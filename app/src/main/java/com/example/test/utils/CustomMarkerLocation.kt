package com.example.test.utils

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import com.example.test.data.model.UserLocationModel

import org.osmdroid.api.IMapController
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystem
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import java.util.*

/**
 *
 * @author Marc Kurtz
 * @author Manuel Stahl
 */
class CustomMarkerLocation(mapView: MapView) : Overlay() {
    // ===========================================================
    // Constants
    // ===========================================================
    // ===========================================================
    // Fields
    // ===========================================================
    protected var mPaint = Paint()
    protected var mCirclePaint: Paint? = Paint()
    protected val mScale: Float
    protected var mPersonBitmap: Bitmap? = null
    protected var mDirectionArrowBitmap: Bitmap? = null
    protected var mMapView: MapView?
    private var mMapController: IMapController?
    var mMyLocationProvider: IMyLocationProvider? = null
    private val mRunOnFirstFix = LinkedList<Runnable>()
    private val mDrawPixel = Point()
    private val mSnapPixel = Point()
    private var mHandler: Handler?
    private var mHandlerToken: Any? = Any()

    /**
     * if true, when the user pans the map, follow my location will automatically disable
     * if false, when the user pans the map, the map will continue to follow current location
     */
    var enableAutoStop = true
    var lastFix: UserLocationModel? = null
        private set
    private val mGeoPoint = GeoPoint(0, 0) // for reuse

    /**
     * If enabled, the map is receiving location updates and drawing your location on the map.
     *
     * @return true if enabled, false otherwise
     */
    var isMyLocationEnabled = false
        private set

    /**
     * If enabled, the map will center on your current location and automatically scroll as you
     * move. Scrolling the map in the UI will disable.
     *
     * @return true if enabled, false otherwise
     */
    var isFollowLocationEnabled = false // follow location updates
        protected set
    /**
     * If enabled, an accuracy circle will be drawn around your current position.
     *
     * @return true if enabled, false otherwise
     */
    /**
     * If enabled, an accuracy circle will be drawn around your current position.
     *
     * @param drawAccuracyEnabled
     * whether the accuracy circle will be enabled
     */
    var isDrawAccuracyEnabled = true

    /** Coordinates the feet of the person are located scaled for display density.  */
    protected val mPersonHotspot: PointF
    protected var mDirectionArrowCenterX = 0f
    protected var mDirectionArrowCenterY = 0f
    private var mOptionsMenuEnabled = true
    private var wasEnabledOnPause = false


    /**
     * fix for https://github.com/osmdroid/osmdroid/issues/249
     * @param personBitmap
     * @param directionArrowBitmap
     */
    fun setDirectionArrow(personBitmap: Bitmap?, directionArrowBitmap: Bitmap?) {
        mPersonBitmap = personBitmap
        mDirectionArrowBitmap = directionArrowBitmap
        mDirectionArrowCenterX = mDirectionArrowBitmap!!.width / 2.0f - 0.5f
        mDirectionArrowCenterY = mDirectionArrowBitmap!!.height / 2.0f - 0.5f
    }

    override fun onResume() {
        super.onResume()
        if (wasEnabledOnPause) enableFollowLocation()
    }

    override fun onPause() {
        wasEnabledOnPause = isFollowLocationEnabled
        super.onPause()
    }

    override fun onDetach(mapView: MapView) {
        /*if (mPersonBitmap != null) {
			mPersonBitmap.recycle();
		}
		if (mDirectionArrowBitmap != null) {
			mDirectionArrowBitmap.recycle();
		}*/mMapView = null
        mMapController = null
        mHandler = null
        mCirclePaint = null
        //mPersonBitmap = null;
        //mDirectionArrowBitmap = null;
        mHandlerToken = null
        lastFix = null
        mMapController = null
        if (mMyLocationProvider != null) mMyLocationProvider!!.destroy()
        mMyLocationProvider = null
        super.onDetach(mapView)
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================


    fun setPersonHotspot(x: Float, y: Float) {
        mPersonHotspot[x] = y
    }

    protected fun drawMyLocation(canvas: Canvas, pj: Projection, lastFix: UserLocationModel) {
        pj.toPixels(mGeoPoint, mDrawPixel)
        if (isDrawAccuracyEnabled) {
            val radius = (lastFix.accuracy
                    / TileSystem.GroundResolution(lastFix.latitude,
                pj.zoomLevel).toFloat())
            mCirclePaint!!.alpha = 50
            mCirclePaint!!.style = Paint.Style.FILL
            canvas.drawCircle(mDrawPixel.x.toFloat(), mDrawPixel.y.toFloat(), radius,
                mCirclePaint!!)
            mCirclePaint!!.alpha = 150
            mCirclePaint!!.style = Paint.Style.STROKE
            canvas.drawCircle(mDrawPixel.x.toFloat(), mDrawPixel.y.toFloat(), radius,
                mCirclePaint!!)
        }
        if (lastFix.hasBearingMask) {
            canvas.save()
            // Rotate the icon if we have a GPS fix, take into account if the map is already rotated
            var mapRotation: Float
            mapRotation = lastFix.bearing
            if (mapRotation >= 360.0f) mapRotation = mapRotation - 360f
            canvas.rotate(mapRotation, mDrawPixel.x.toFloat(), mDrawPixel.y.toFloat())
            // Draw the bitmap
            canvas.drawBitmap(mDirectionArrowBitmap!!, mDrawPixel.x
                    - mDirectionArrowCenterX, mDrawPixel.y - mDirectionArrowCenterY,
                mPaint)
            canvas.restore()
        } else {
            canvas.save()
            // Unrotate the icon if the maps are rotated so the little man stays upright
            canvas.rotate(-mMapView!!.mapOrientation, mDrawPixel.x.toFloat(),
                mDrawPixel.y.toFloat())
            // Draw the bitmap
            canvas.drawBitmap(mPersonBitmap!!, mDrawPixel.x - mPersonHotspot.x,
                mDrawPixel.y - mPersonHotspot.y, mPaint)
            canvas.restore()
        }
    }

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================
    override fun draw(c: Canvas, pProjection: Projection) {
        if (lastFix != null ) {
            drawMyLocation(c, pProjection, lastFix!!)
        }
    }



    override fun onTouchEvent(event: MotionEvent, mapView: MapView): Boolean {
        val isSingleFingerDrag = (event.action == MotionEvent.ACTION_MOVE
                && event.pointerCount == 1)
        if (event.action == MotionEvent.ACTION_DOWN && enableAutoStop) {
            disableFollowLocation()
        } else if (isSingleFingerDrag && isFollowLocationEnabled) {
            return true // prevent the pan
        }
        return super.onTouchEvent(event, mapView)
    }


    // ===========================================================
    // Methods
    // ===========================================================
    /**
     * Return a GeoPoint of the last known location, or null if not known.
     */


    /**
     * Enables "follow" functionality. The map will center on your current location and
     * automatically scroll as you move. Scrolling the map in the UI will disable.
     */
    fun enableFollowLocation() {
        isFollowLocationEnabled = true
        // Update the screen to see changes take effect
    }

    /**
     * Disables "follow" functionality.
     */
    fun disableFollowLocation() {
        mMapController!!.stopAnimation(false)
        isFollowLocationEnabled = false
    }


     fun setLocation(location: UserLocationModel?) {
        lastFix = location
        mGeoPoint.setCoords(lastFix!!.latitude, lastFix!!.longitude)
         if (mMapView != null) {
            mMapView!!.postInvalidate()
        }
    }


    fun setPersonIcon(icon: Bitmap?) {
        mPersonBitmap = icon
    }

    companion object {
        val MENU_MY_LOCATION = getSafeMenuId()
    }

    init {
        mScale = mapView.context.resources.displayMetrics.density
        mMapView = mapView
        mMapController = mapView.controller
        mCirclePaint!!.setARGB(0, 100, 100, 255)
        mCirclePaint!!.isAntiAlias = true
        mPaint.isFilterBitmap = true
        setDirectionArrow((mapView.context.resources.getDrawable(R.drawable.person) as BitmapDrawable).bitmap,
            (mapView.context.resources.getDrawable(R.drawable.twotone_navigation_black_48) as BitmapDrawable).bitmap)

        // Calculate position of person icon's feet, scaled to screen density
        mPersonHotspot = PointF(24.0f * mScale + 0.5f, 39.0f * mScale + 0.5f)
        mHandler = Handler(Looper.getMainLooper())
    }
}