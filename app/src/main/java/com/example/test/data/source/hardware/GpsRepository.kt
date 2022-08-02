package com.example.test.data.source.hardware

import android.location.Location
import com.example.test.data.source.gps.GpsListener
import kotlinx.coroutines.flow.Flow

interface GpsRepository {

    fun startListen(minUpdatePeriod: Long, minUpdateDistance: Float)

    fun getLastKnownLocation(): Flow<Location?>

    fun stopListen()

    fun requestCurrentLocation()

    fun setLocationListener(listener: GpsListener?)

    fun hasProvider(): Boolean
}