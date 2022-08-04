package com.example.test.presentation

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation


import com.example.test.App.Companion.appComponentMain
import com.example.test.BuildConfig
import com.example.test.R
import com.example.test.data.model.UserPointModel
import com.example.test.databinding.ActivityMainBinding
import com.example.test.di.mainActivtiy.DaggerMainActvitityComponent
import com.example.test.di.mainActivtiy.DaggerMainActvitityComponentTest
import com.example.test.di.mainActivtiy.MainActvitityComponent
import com.example.test.di.mainActivtiy.MainActvitityComponentTest
import com.example.test.presentation.infoFragment.InfoFragmentDirections
import com.example.test.services.LocationService
import com.example.test.services.LocationServiceListener
import com.example.test.utils.*
import kotlinx.coroutines.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class MainActivity : BaseActivity(), CoroutineScope {
    lateinit var activityComponent: MainActvitityComponent
    lateinit var activityComponentTest: MainActvitityComponentTest

    @Inject
    lateinit var factory: FactoryMainView
    private val viewModelMain by viewModels<MainActivityViewModel> { factory }
    private lateinit var binding: ActivityMainBinding
    private var firstOpen = true
    private lateinit var navController: NavController

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private lateinit var mapController: IMapController
    private var context: MainActivity? = null
    override fun onAfterRequestPermission() {
        //  TODO("Not yet implemented")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        if (!BuildConfig.DEBUG) {
            activityComponent = DaggerMainActvitityComponent.factory().create(appComponentMain)
            activityComponent.inject(this)
        } else {
            activityComponentTest =
                DaggerMainActvitityComponentTest.factory().create(appComponentMain)
            activityComponentTest.inject(this)
        }
        super.onCreate(savedInstanceState)
        context = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        initMap()
        initFlowMylocation()
        initFlowTapMarker()
        initFlowUserPoint()
        initFlowError()
        edgeToEdge()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initMap() {
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        // человечек на карте
        val locationOverlay = CustomMarkerLocation(binding.map)
        locationOverlay.setDirectionArrow(context!!.resources.getDrawable(R.drawable.ic_mylocation_55dp).toBitmap(),null)
        binding.map.overlays.add(locationOverlay)
        // повороты
        val rotationGestureOverlay = RotationGestureOverlay(this, binding.map)
        rotationGestureOverlay.isEnabled
        binding.map.setMultiTouchControls(true)
        binding.map.overlays.add(rotationGestureOverlay)
    }


    private fun initFlowMylocation() {
        lifecycleScope.launchWhenResumed {
            viewModelMain.stateFlowCoordinate.collect {
                when (it) {
                    is ResponseDataBase.SuccessNotList -> {
                        val location = (it.value)
                        if (firstOpen && !viewModelMain.focusFlag) {
                            Log.d("animateLocation", (it.value)!!.latitude.toString())
                            mapController.setZoom(15.0)
                            mapController.animateTo(
                                GeoPoint(
                                    location!!.latitude,
                                    location.longitude
                                )
                            )
                            firstOpen = false
                        }
                        Log.d("kek", (it.value)!!.latitude.toString())
                        (binding.map.overlays[0] as CustomMarkerLocation).setLocation(location)
                        binding.map.visibility = View.VISIBLE
                    }
                    is ResponseDataBase.Failure -> {
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initFlowError() {
        lifecycleScope.launchWhenResumed {
            viewModelMain.sharedFlowError.collect {
                when (it) {
                    is ErrorApp.FailureDataBase -> {
                        Toast.makeText(
                            context, getString(R.string.database_error, it.value.toString()),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    is ErrorApp.FailureUnknown -> {
                        Toast.makeText(
                            context,
                            getString(R.string.unknown_error, it.value.toString()),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    is ErrorApp.FailureLocation -> {
                        val toast = Toast.makeText(
                            applicationContext,
                            getString(R.string.location_error, it.value.toString()),
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initFlowUserPoint() {
        lifecycleScope.launchWhenResumed {
            viewModelMain.sharedFlowUserPoint.collect {
                when (it) {
                    is ResponseDataBase.SuccessList -> {
                        paintUserPoints(it.value)
                    }
                    else -> {}
                }
            }
        }
    }


    private fun initFlowTapMarker() {
        lifecycleScope.launchWhenResumed {
            viewModelMain.stateFlowPhocus.collect {
                when (it) {
                    is ResponsePhocus.Success -> {
                        viewModelMain.focusFlag = true
                        transitionToInfo(it.value)
                        mapController.setZoom(16.0)
                        binding.map.controller.animateTo(it.value.position)
                        viewModelMain.flagIsOpenForTransition = false
                    }
                    is ResponsePhocus.Clear -> {
                        if (viewModelMain.flagIsOpen) {
                            viewModelMain.focusFlag = false
                            viewModelMain.flagIsOpen = false
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun transitionToInfo(response:CustomMarker){
        if (!viewModelMain.flagIsOpen) {
            val action = EmptyFragmentDirections.actionNavigationHomeToInfoFragment(
                response.userPointModel.name,
                response.userPointModel.coordinateProvider!!,
                response.userPointModel.date!!.time,
                response.userPointModel.time!!,
                response.userPointModel.img!!
            )
            navController.navigate(action)
            viewModelMain.flagIsOpen = true
        }else{
            viewModelMain.flagIsOpenForTransition = true
            onBackPressed()
            val action = EmptyFragmentDirections.actionNavigationHomeToInfoFragment(
                response.userPointModel.name,
                response.userPointModel.coordinateProvider!!,
                response.userPointModel.date!!.time,
                response.userPointModel.time!!,
                response.userPointModel.img!!
            )
            navController.navigate(action)
            viewModelMain.flagIsOpen = true
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun paintUserPoints(listUserPoint: List<UserPointModel>) {
        listUserPoint.forEach {
            val startPoint = GeoPoint(it!!.latitude, it.longitude)
            val startMarker = CustomMarker(binding.map, userPointModel = it)
            // val infoWindow = InfoWindow(R.layout.bonuspack_bubble,binding.map)
            startMarker.position = startPoint
            startMarker.setInfoWindow(null)
            startMarker.setOnMarkerClickListener(clickListener(viewModelMain))
            //  startMarker.setInfoWindow()
            startMarker.setAnchor(CustomMarker.ANCHOR_CENTER, CustomMarker.ANCHOR_BOTTOM)
            startMarker.iconBackground = this.getDrawable(R.drawable.ic_tracker_75dp)
            if (BuildConfig.DEBUG)
                startMarker.icon = when (it.img) {
                    "1" -> {
                        this.getDrawable(R.drawable.svidetel)
                    }
                    "2" -> {
                        this.getDrawable(R.drawable.gendalf)
                    }
                    "3" -> {
                        this.getDrawable(R.drawable.oxl_vs)
                    }
                    else -> {
                        this.getDrawable(R.drawable.svidetel)
                    }
                }
            binding.map.overlays.add(startMarker)
        }
        binding.map.invalidate()
    }

    private fun clickListener(
        viewModel: MainActivityViewModel
    ): CustomMarker.OnMarkerClickListener {
        return ListenerMarker(viewModel)
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
        Log.d("Main", "onPause")
        LocationService.stopService(this)
        LocationService.customUnbindService(this, this)
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        mapController = binding.map.controller
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        (navHostFragment!!.childFragmentManager.primaryNavigationFragment as? OnBackPressedFrament)?.onBack()
            ?.let {
                if(!viewModelMain.flagIsOpenForTransition)
                viewModelMain.clearMarker()
                if (!it) super.onBackPressed()
            }
    }

    override fun locationThrowable(throwable: Throwable) {
        viewModelMain.errorLocation(throwable)
    }

}

interface OnBackPressedFrament {
    fun onBack(): Boolean
}

