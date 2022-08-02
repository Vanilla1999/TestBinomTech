package com.example.test.data.source.gps

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.example.test.di.ApplicationContext
import javax.inject.Inject


class GpsDataSourceImpl @Inject constructor( @ApplicationContext context: Context) : GpsDataSource,
    InnerLocationListener {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var gpsListener: GpsListener? = null
    private var location: Location? = null

    private val customLocationListener =
        CustomLocationListener("gps", this, this::requestCurrentLocation)

    private var minUpdatePeriod = 100L
    private var minUpdateDistance = 100F

    override fun onStartListen(minUpdatePeriod: Long, minUpdateDistance: Float) {
        try {
            //locationManager.removeUpdates(multiLocationListener)
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                this.minUpdatePeriod = minUpdatePeriod
                this.minUpdateDistance = minUpdateDistance
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minUpdatePeriod,
                    minUpdateDistance,
                    customLocationListener
                )
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) {
            location = null
            locationFailure(e)
        } catch (e: Exception) {
            locationFailure(e)
        }
    }

    override fun onStopListen() {
        locationManager.removeUpdates(customLocationListener)
    }

    override fun requestCurrentLocation() {
        try {
            locationManager.removeUpdates(customLocationListener)
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minUpdatePeriod,
                    minUpdateDistance,
                    customLocationListener
                )
            }
        } catch (e: SecurityException) {
            locationFailure(e)
        } catch (e: Exception) {
            locationFailure(e)
        }
    }

    override fun getLastKnownLocation(): Location? {
        return try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        } catch (e: SecurityException) {
            null
        }
    }

    override fun setListener(listener: GpsListener?) {
        this.gpsListener = listener
    }

    override fun hasProvider(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun locationUpdate(newLocation: Location, name: String) {
        gpsListener?.onLocationUpdate(newLocation)
    }

    override fun locationFailure(throwable: Throwable) {
        gpsListener?.onLocationFailure(throwable)
    }
}


private class CustomLocationListener(
    val name: String,
    val innerLocationListener: InnerLocationListener,
    val onEnable: () -> Unit,
) : LocationListener {

    override fun onLocationChanged(newLocation: Location) =
        innerLocationListener.locationUpdate(newLocation, name)

    override fun onProviderEnabled(provider: String) = onEnable.invoke()

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {}
    override fun onProviderDisabled(provider: String) {}
}

private interface InnerLocationListener {
    fun locationUpdate(newLocation: Location, name: String)
    fun locationFailure(throwable: Throwable)

}