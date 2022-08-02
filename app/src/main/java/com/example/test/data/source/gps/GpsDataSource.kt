package com.example.test.data.source.gps

import android.location.Location

interface GpsDataSource {

    fun onStartListen(minUpdatePeriod: Long, minUpdateDistance: Float)

    fun onStopListen()

    fun requestCurrentLocation()

    fun getLastKnownLocation(): Location?

    fun setListener(listener: GpsListener?)

    fun hasProvider(): Boolean
}

interface GpsListener {

    fun onLocationUpdate(location: Location)

    fun onLocationFailure(throwable: Throwable)
}