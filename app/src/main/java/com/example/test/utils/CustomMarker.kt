package com.example.test.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MotionEvent
import org.osmdroid.tileprovider.BitmapPool
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.RectL
import org.osmdroid.views.MapView
import org.osmdroid.views.MapViewRepository
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.OverlayWithIW
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow


/**
 * A marker is an icon placed at a particular point on the map's surface that can have a popup-[org.osmdroid.views.overlay.infowindow.InfoWindow] (a bubble)
 * Mimics the Marker class from Google Maps Android API v2 as much as possible. Main differences:<br></br>
 *
 * - Doesn't support Z-Index: as other osmdroid overlays, Marker is drawn in the order of appearance. <br></br>
 * - The icon can be any standard Android Drawable, instead of the BitmapDescriptor introduced in Google Maps API v2. <br></br>
 * - The icon can be changed at any time. <br></br>
 * - The InfoWindow hosts a standard Android View. It can handle Android widgets like buttons and so on. <br></br>
 * - Supports a "sub-description", to be displayed in the InfoWindow, under the snippet, in a smaller text font. <br></br>
 * - Supports an image, to be displayed in the InfoWindow. <br></br>
 * - Supports "panning to view" on/off option (when touching a marker, center the map on marker position). <br></br>
 * - Opening a Marker InfoWindow automatically close others only if it's the same InfoWindow shared between Markers. <br></br>
 * - Events listeners are set per marker, not per map. <br></br>
 *
 * <img alt="Class diagram around Marker class" width="686" height="413" src='src=' .></img>/doc-files/marker-infowindow-classes.png' />
 *
 * @see MarkerInfoWindow
 * see also [Google Maps Marker](http://developer.android.com/reference/com/google/android/gms/maps/model/Marker.html)
 *
 *
 * @author M.Kergall
 */
class CustomMarker @JvmOverloads constructor(
    mapView: MapView,
    resourceProxy: Context? = mapView.context
) :
    OverlayWithIW() {
    /**
     * used for when the icon is explicitly set to null and the title is not, this will
     * style the rendered text label
     * @return
     */
    /**
     * used for when the icon is explicitly set to null and the title is not, this will
     * style the rendered text label
     * @return
     */
    /* attributes for text labels, used for osmdroid gridlines */
    var textLabelBackgroundColor = Color.WHITE
    /**
     * used for when the icon is explicitly set to null and the title is not, this will
     * style the rendered text label
     * @return
     */
    /**
     * used for when the icon is explicitly set to null and the title is not, this will
     * style the rendered text label
     * @return
     */
    var textLabelForegroundColor = Color.BLACK
    /**
     * used for when the icon is explicitly set to null and the title is not, this will
     * style the rendered text label
     * @return
     */
    /**
     * used for when the icon is explicitly set to null and the title is not, this will
     * style the rendered text label
     * @return
     */
    var textLabelFontSize = 24

    /*attributes for standard features:*/
    protected var mIcon: Drawable? = null
    protected var mIconBackground: Drawable? = null
    protected var mPosition: GeoPoint

    /**
     * rotates the icon in relation to the map
     * @param rotation
     */
    var rotation: Float
    protected var mAnchorU: Float
    protected var mAnchorV: Float
    protected var mIWAnchorU: Float
    protected var mIWAnchorV: Float
    var alpha: Float
    var isDraggable: Boolean
    protected var mIsDragged: Boolean
    var isFlat: Boolean
    protected var mOnMarkerClickListener: OnMarkerClickListener?
    protected var mOnMarkerDragListener: OnMarkerDragListener?
    /** get the image to be shown in the InfoWindow - this is not the marker icon  */
    /** set an image to be shown in the InfoWindow  - this is not the marker icon  */
    /*attributes for non-standard features:*/
    var image: Drawable? = null
    protected var mPanToView: Boolean
    /** get the offset in millimeters that the marker is moved up while dragging  */
    /** set the offset in millimeters that the marker is moved up while dragging  */
    var dragOffset: Float

    /*internals*/
    protected var mPositionPixels: Point
    protected var mResources: Resources?

    /**
     * @since 6.0.3
     */
    private var mMapViewRepository: MapViewRepository?
    /**
     * @since 6.0.3
     */
    /**
     * @since 6.0.3
     */
    var isDisplayed = false
        private set
    private val mRect = Rect()
    private val mOrientedMarkerRect = Rect()
    private var mPaint: Paint? = null

    /**
     * @since 6.0.3
     */
    fun setDefaultIcon() {
        mIcon = mMapViewRepository!!.defaultMarkerIcon
        setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM)
    }

    /**
     * @since 6.0.3
     */
    fun setTextIcon(pText: String?) {
        val background = Paint()
        background.color = textLabelBackgroundColor
        val p = Paint()
        p.textSize = textLabelFontSize.toFloat()
        p.color = textLabelForegroundColor
        p.isAntiAlias = true
        p.typeface = Typeface.DEFAULT_BOLD
        p.textAlign = Paint.Align.LEFT
        val width = (p.measureText(pText) + 0.5f).toInt()
        val baseline = (-p.ascent() + 0.5f).toInt().toFloat()
        val height = (baseline + p.descent() + 0.5f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(image)
        c.drawPaint(background)
        c.drawText(pText!!, 0f, baseline, p)
        mIcon = BitmapDrawable(mResources, image)
        setAnchor(ANCHOR_CENTER, ANCHOR_CENTER)
    }
    /**
     *
     * @since 6.0.0?
     * @return
     */
    /** Sets the icon for the marker. Can be changed at any time.
     * This is used on the map view.
     * The anchor will be left unchanged; you may need to call [.setAnchor]
     * Two exceptions:
     * - for text icons, the anchor is set to (center, center)
     * - for the default icon, the anchor is set to the corresponding position (the tip of the teardrop)
     * Related methods: [.setTextIcon], [.setDefaultIcon] and [.setAnchor]
     * @param icon if null, the default osmdroid marker is used.
     */
    var icon: Drawable?
        get() = mIcon
        set(icon) {
            if (icon != null) {
                mIcon = icon
            } else {
                setDefaultIcon()
            }
        }
    var iconBackground: Drawable?
        get() = mIconBackground
        set(icon) {
            if (icon != null) {
                mIconBackground = icon
            } else {
                setDefaultIcon()
            }
        }

    /**
     * sets the location on the planet where the icon is rendered
     * @param position
     */
    var position: GeoPoint
        get() = mPosition
        set(position) {
            mPosition = position.clone()
            if (isInfoWindowShown) {
                closeInfoWindow()
                showInfoWindow()
            }
            mBounds = BoundingBox(
                position.latitude,
                position.longitude,
                position.latitude,
                position.longitude
            )
        }

    /**
     *
     * @param anchorU WIDTH 0.0-1.0 percentage of the icon that offsets the logical center from the actual pixel center point
     * @param anchorV HEIGHT 0.0-1.0 percentage of the icon that offsets the logical center from the actual pixel center point
     */
    fun setAnchor(anchorU: Float, anchorV: Float) {
        mAnchorU = anchorU
        mAnchorV = anchorV
    }

    fun setInfoWindowAnchor(anchorU: Float, anchorV: Float) {
        mIWAnchorU = anchorU
        mIWAnchorV = anchorV
    }

    /**
     * Removes this Marker from the MapView.
     * Note that this method will operate only if the Marker is in the MapView overlays
     * (it should not be included in a container like a FolderOverlay).
     * @param mapView
     */
    fun remove(mapView: MapView) {
        mapView.overlays.remove(this)
    }

    fun setOnMarkerClickListener(listener: OnMarkerClickListener?) {
        mOnMarkerClickListener = listener
    }

    fun setOnMarkerDragListener(listener: OnMarkerDragListener?) {
        mOnMarkerDragListener = listener
    }

    /** Set the InfoWindow to be used.
     * Default is a MarkerInfoWindow, with the layout named "bonuspack_bubble".
     * You can use this method either to use your own layout, or to use your own sub-class of InfoWindow.
     * Note that this InfoWindow will receive the Marker object as an input, so it MUST be able to handle Marker attributes.
     * If you don't want any InfoWindow to open, you can set it to null.  */
    fun setInfoWindow(infoWindow: MarkerInfoWindow) {
        mInfoWindow = infoWindow
    }

    /** If set to true, when clicking the marker, the map will be centered on the marker position.
     * Default is true.  */
    fun setPanToView(panToView: Boolean) {
        mPanToView = panToView
    }

    /**
     * shows the info window, if it's open, this will close and reopen it
     */
    fun showInfoWindow() {
        if (mInfoWindow == null) return
        val markerWidth = mIcon!!.intrinsicWidth
        val markerHeight = mIcon!!.intrinsicHeight
        val offsetX = (markerWidth * (mIWAnchorU - mAnchorU)).toInt()
        val offsetY = (markerHeight * (mIWAnchorV - mAnchorV)).toInt()
        if (rotation == 0f) {
            mInfoWindow.open(this, mPosition, offsetX, offsetY)
            return
        }
        val centerX = 0
        val centerY = 0
        val radians = -rotation * Math.PI / 180.0
        val cos = Math.cos(radians)
        val sin = Math.sin(radians)
        val rotatedX = RectL.getRotatedX(
            offsetX.toLong(),
            offsetY.toLong(),
            centerX.toLong(),
            centerY.toLong(),
            cos,
            sin
        ).toInt()
        val rotatedY = RectL.getRotatedY(
            offsetX.toLong(),
            offsetY.toLong(),
            centerX.toLong(),
            centerY.toLong(),
            cos,
            sin
        ).toInt()
        mInfoWindow.open(this, mPosition, rotatedX, rotatedY)
    }

    val isInfoWindowShown: Boolean
        get() = if (mInfoWindow is MarkerInfoWindow) {
            val iw = mInfoWindow as MarkerInfoWindow
            iw != null && iw.isOpen && iw.markerReference.equals( this)
        } else super.isInfoWindowOpen()

    override fun draw(canvas: Canvas, pj: Projection) {
        if (mIcon == null) return
        if (!isEnabled) return
        pj.toPixels(mPosition, mPositionPixels)
        val rotationOnScreen = if (isFlat) -rotation else -pj.orientation - rotation
        drawAt(canvas, mPositionPixels.x, mPositionPixels.y, rotationOnScreen)
        if (isInfoWindowShown) {
            //showInfoWindow();
            mInfoWindow.draw()
        }
    }

    /** Null out the static references when the MapView is detached to prevent memory leaks.  */
    override fun onDetach(mapView: MapView) {
        BitmapPool.getInstance().asyncRecycle(mIcon)
        mIcon = null
        BitmapPool.getInstance().asyncRecycle(image)
        //cleanDefaults();
        mOnMarkerClickListener = null
        mOnMarkerDragListener = null
        mResources = null
        relatedObject = null
        if (isInfoWindowShown) closeInfoWindow()
        //	//if we're using the shared info window, this will cause all instances to close
        mMapViewRepository = null
        infoWindow = null
        onDestroy()
        super.onDetach(mapView)
    }

    fun hitTest(event: MotionEvent, mapView: MapView?): Boolean {
        return mIcon != null && isDisplayed && mOrientedMarkerRect.contains(
            event.x.toInt(),
            event.y.toInt()
        ) // "!=null": fix for #1078
    }

    override fun onSingleTapConfirmed(event: MotionEvent, mapView: MapView): Boolean {
        val touched = hitTest(event, mapView)
        return if (touched) {
            if (mOnMarkerClickListener == null) {
                onMarkerClickDefault(this, mapView)
            } else {
                mOnMarkerClickListener!!.onMarkerClick(this, mapView)
            }
        } else touched
    }

    fun moveToEventPosition(event: MotionEvent, mapView: MapView) {
        val offsetY = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_MM,
            dragOffset, mapView.context.resources.displayMetrics
        )
        val pj = mapView.projection
        position =
            pj.fromPixels(event.x.toInt(), (event.y - offsetY).toInt()) as GeoPoint
        mapView.invalidate()
    }

    override fun onLongPress(event: MotionEvent, mapView: MapView): Boolean {
        val touched = hitTest(event, mapView)
        if (touched) {
            if (isDraggable) {
                //starts dragging mode:
                mIsDragged = true
                closeInfoWindow()
                if (mOnMarkerDragListener != null) mOnMarkerDragListener!!.onMarkerDragStart(this)
                moveToEventPosition(event, mapView)
            }
        }
        return touched
    }

    override fun onTouchEvent(event: MotionEvent, mapView: MapView): Boolean {
        return if (isDraggable && mIsDragged) {
            if (event.action == MotionEvent.ACTION_UP) {
                mIsDragged = false
                if (mOnMarkerDragListener != null) mOnMarkerDragListener!!.onMarkerDragEnd(this)
                true
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                moveToEventPosition(event, mapView)
                if (mOnMarkerDragListener != null) mOnMarkerDragListener!!.onMarkerDrag(this)
                true
            } else false
        } else false
    }

    fun setVisible(visible: Boolean) {
        if (visible) alpha = 1f else alpha = 0f
    }

    //-- Marker events listener interfaces ------------------------------------
    interface OnMarkerClickListener {
        fun onMarkerClick(marker: CustomMarker, mapView: MapView): Boolean
    }

    interface OnMarkerDragListener {
        fun onMarkerDrag(marker: CustomMarker?)
        fun onMarkerDragEnd(marker: CustomMarker?)
        fun onMarkerDragStart(marker: CustomMarker?)
    }

    /** default behaviour when no click listener is set  */
    protected fun onMarkerClickDefault(marker: CustomMarker, mapView: MapView): Boolean {
        marker.showInfoWindow()
        if (marker.mPanToView) mapView.controller.animateTo(marker.position)
        return true
    }

    /**
     * Optimized drawing
     * @since 6.0.3
     */
    protected fun drawAt(pCanvas: Canvas, pX: Int, pY: Int, pOrientation: Float) {
        val markerWidth =  mIconBackground!!.intrinsicWidth
        val markerHeight =  mIconBackground!!.intrinsicHeight
        val markerWidthIcon =mIcon!!.intrinsicWidth
        val markerHeightIcon =mIcon!!.intrinsicHeight
        val offsetX = pX - Math.round(markerWidth * mAnchorU)
        val offsetY = pY - Math.round(markerHeight * mAnchorV)
        mRect[offsetX, offsetY, offsetX + markerWidth] = offsetY + markerHeight
        RectL.getBounds(mRect, pX, pY, pOrientation.toDouble(), mOrientedMarkerRect)
        isDisplayed = Rect.intersects(mOrientedMarkerRect, pCanvas.clipBounds)
        if (!isDisplayed) { // optimization 1: (much faster, depending on the proportions) don't try to display if the Marker is not visible
            return
        }
        if (alpha == 0f) {
            return
        }
        if (pOrientation != 0f) { // optimization 2: don't manipulate the Canvas if not needed (about 25% faster) - step 1/2
            pCanvas.save()
            pCanvas.rotate(pOrientation, pX.toFloat(), pY.toFloat())
        }
        if (mIcon is BitmapDrawable) { // optimization 3: (about 15% faster)
            val paint: Paint?
            if (alpha == 1f) {
                paint = null
            } else {
                if (mPaint == null) {
                    mPaint = Paint()
                }
                mPaint!!.alpha = (alpha * 255).toInt()
                paint = mPaint
            }
            mIconBackground!!.alpha = (alpha * 255).toInt()
            mIconBackground!!.bounds = mRect
            mIconBackground!!.draw(pCanvas)

            pCanvas.drawBitmap(
                getCroppedBitmap((mIcon as BitmapDrawable).bitmap,offsetX,offsetY,65)!! ,
                offsetX.toFloat()+markerWidth/5  ,
                offsetY.toFloat() + markerHeight/7,
                paint
            )
        } else {

            mIconBackground!!.alpha = (alpha * 255).toInt()
            mIconBackground!!.bounds = mRect
            mIconBackground!!.draw(pCanvas)
            mIcon!!.alpha = (alpha * 255).toInt()
            mIcon!!.bounds = mRect
            mIcon!!.draw(pCanvas)
        }
        if (pOrientation != 0f) { // optimization 2: step 2/2
            pCanvas.restore()
        }
    }
    private fun getCroppedBitmap(bitmap: Bitmap, cx: Int, cy: Int, radius: Int): Bitmap? {
        val diam = radius shl 1
        val targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(targetBitmap)
        val color = -0xbdbdbe
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, radius, radius, false), ( -radius/6).toFloat(), ( 0).toFloat(), paint)
        return targetBitmap
    }
    companion object {
        /** Usual values in the (U,V) coordinates system of the icon image  */
        const val ANCHOR_CENTER = 0.5f
        const val ANCHOR_LEFT = 0.0f
        const val ANCHOR_TOP = 0.0f
        const val ANCHOR_RIGHT = 1.0f
        const val ANCHOR_BOTTOM = 1.0f

        /**
         * Prevent memory leaks and call this when you're done with the map
         * reference https://github.com/MKergall/osmbonuspack/pull/210
         */
        @Deprecated("")
        fun cleanDefaults() {
        }
    }

    init {
        mMapViewRepository = mapView.repository
        mResources = mapView.context.resources
        rotation = 0.0f
        alpha = 1.0f //opaque
        mPosition = GeoPoint(0.0, 0.0)
        mAnchorU = ANCHOR_CENTER
        mAnchorV = ANCHOR_CENTER
        mIWAnchorU = ANCHOR_CENTER
        mIWAnchorV = ANCHOR_TOP
        isDraggable = false
        mIsDragged = false
        mPositionPixels = Point()
        mPanToView = true
        dragOffset = 0.0f
        isFlat = false //billboard
        mOnMarkerClickListener = null
        mOnMarkerDragListener = null
        setDefaultIcon()
        setInfoWindow(mMapViewRepository!!.defaultMarkerInfoWindow)
    }
}