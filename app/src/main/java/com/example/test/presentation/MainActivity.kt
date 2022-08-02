package com.example.test.presentation

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels

import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test.App.Companion.appComponentMain
import com.example.test.data.model.UserPointModel
import com.example.test.databinding.ActivityMainBinding
import com.example.test.di.mainActivtiy.DaggerMainActvitityComponent
import com.example.test.di.mainActivtiy.MainActvitityComponent
import com.example.test.services.LocationService
import com.example.test.utils.CustomMarkerLocation
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class MainActivity : BaseActivity(), ServiceConnection, CoroutineScope {
    lateinit var activityComponent: MainActvitityComponent

    @Inject
    lateinit var factory: FactoryMainView
    private val viewModelMain by viewModels<MainActivityViewModel> { factory }
    private  val binding: ActivityMainBinding by viewBinding()
    private var locationService: LocationService? = null
    private var locationUpdatesJob: Job? = null
    private var firstOpen = true
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private lateinit var mapController: IMapController
    private var context: MainActivity? = null
    override fun onAfterRequestPermission() {
        //  TODO("Not yet implemented")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("onTouchEvent", "MainActivity")
        return super.onTouchEvent(event)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerMainActvitityComponent.factory().create(appComponentMain)
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        context = this
        setContentView(binding.root)
        initMap()
        initFlowDatabase()
        intiFlowError()
        //viewModelMain.mockDatabase(this)

    }

    private fun initMap() {
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        // человечек на карте
        val locationOverlay = CustomMarkerLocation(binding.map)
        binding.map.overlays.add(locationOverlay)
        // повороты
        val rotationGestureOverlay = RotationGestureOverlay(this, binding.map)
        rotationGestureOverlay.isEnabled
        binding.map.setMultiTouchControls(true)
        binding.map.overlays.add(rotationGestureOverlay)
    }


    private fun initFlowMylocation() {

    }

    private fun intiFlowError() {

    }

    private fun initFlowDatabase() {

    }


    private fun clickListener(marker: Marker, mapView: MapView, type:UserPointModel): Marker.OnMarkerClickListener {
        return Marker.OnMarkerClickListener { _, _ ->
            true
        }
    }


    private fun clearMarkers() {

    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
        Log.d("Main", "onPause")
        LocationService.stopService(this)
        LocationService.customUnbindService(this, this)
        locationService = null
        locationUpdatesJob?.let {
            cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        mapController = binding.map.controller
        LocationService.startLocation(this)
        LocationService.customBindService(this, this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d("onServiceConnected", "service подключен")
        service as LocationService.LocationServiceBinder
        locationService = service.getService()
        initFlowMylocation()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d("onServiceDisconnected", "service onServiceDisconnected")
        locationService = null
        locationService?.locationServiceListener
    }

    override fun onBackPressed() {
      //TODO
    }

}
interface OnBackPressedFrament{
    fun onBack():Boolean
}
