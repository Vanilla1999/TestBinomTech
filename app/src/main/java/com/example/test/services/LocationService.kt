package com.example.test.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.test.data.CoordinatesRepository
import com.example.test.data.model.UserLocationModel
import com.example.test.data.source.gps.GpsListener
import com.example.test.data.source.hardware.GpsRepository
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


class LocationService : Service(), CoroutineScope, GpsListener {
    private val binder: IBinder = LocationServiceBinder()

    var locationServiceListener: LocationServiceListener? = null
    lateinit var gpsRepository: GpsRepository
    lateinit var coordinatesRepository: CoordinatesRepository
    private val minUpdateTimeSeconds = 40L
    private val minDistanceMeters = 1F

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main

    override fun onCreate() {
        Log.d("onCreateLocatoin", "service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extras = intent?.extras
        val command = extras?.getInt(SERVICE_TASK) ?: START_SERVICE

        when (command) {
            START_SERVICE -> {
                Log.e("START_SERVICE", " Координата записывается")
                initLocationSearching()
            }
            STOP_SERVICE ->
                stopSelf()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // initLocationSearching()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class LocationServiceBinder : Binder() {
        fun getService(): LocationService {
            return this@LocationService
        }
    }

    override fun onDestroy() {
        stopListenLocation()
        job.cancelChildren()
        super.onDestroy()
    }

    private fun stopListenLocation() {
        gpsRepository.setLocationListener(null)
        gpsRepository.stopListen()
    }

    private fun initLocationSearching() {
        gpsRepository.setLocationListener(this)
        gpsRepository.startListen(minUpdateTimeSeconds, minDistanceMeters)
        //gpsRepository.requestCurrentLocation()
    }


    private fun saveUserCoordinates(location: Location) {
        async(Dispatchers.IO) {
            coordinatesRepository.insert(
                UserLocationModel(
                    Date().time / 1000,
                    location.latitude,
                    location.longitude,
                    location.provider,
                    location.accuracy,
                    location.bearing,
                    location.hasBearing()
                )
            )
        }.invokeOnCompletion {

        }
    }

    companion object {
        const val TAG = "LocationService"

        private const val SERVICE_TASK = "SERVICE_TASK"
        private const val START_SERVICE = 100
        private const val STOP_SERVICE = 101
        private const val STOP_CHECK = 101


        @JvmStatic
        fun customBindService(context: Context, connection: ServiceConnection) {
            val intent = Intent(context, LocationService::class.java)
            Log.e("customBindService", " customBindService ")
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        @JvmStatic
        fun customUnbindService(context: Context, connection: ServiceConnection) {
            Log.e("customUnbindService", " customUnbindService ")
            context.unbindService(connection)
        }


        @JvmStatic
        fun startLocation(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            intent.putExtra(SERVICE_TASK, START_SERVICE)
            Log.e("startLocation", " startLocation ")
            context.startService(intent)
        }

        @JvmStatic
        fun stopService(context: Context) {
            val intent = Intent(context, LocationService::class.java)
            Log.e("stopService", " stopService ")
            context.stopService(intent)
        }
    }

    override fun onLocationUpdate(location: Location) {
        launch(Dispatchers.IO) {
            if (location.accuracy < 200) {
                saveUserCoordinates(location)
                Log.d(
                    "LocationUpdate",
                    location.latitude.toString() + " Координата, хорошая"
                )
            } else {
                Log.d(
                    "LocationUpdateWitoutChk",
                    location.latitude.toString() + " координата кривая"
                )
            }
        }
    }


    override fun onLocationFailure(throwable: Throwable) {
        locationServiceListener?.let { it.locationThrowable() }
    }

}

interface LocationServiceListener {
    fun locationThrowable()
}
