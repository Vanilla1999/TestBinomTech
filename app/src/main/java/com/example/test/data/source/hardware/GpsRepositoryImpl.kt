package com.example.test.data.source.hardware

import android.location.Location

import com.example.test.data.source.gps.GpsDataSource
import com.example.test.data.source.gps.GpsListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GpsRepositoryImpl  constructor(
    private val gpsDataSource: GpsDataSource,
) : GpsRepository {
    override fun startListen(minUpdatePeriod: Long, minUpdateDistance: Float) =
        gpsDataSource.onStartListen(minUpdatePeriod, minUpdateDistance)

    override fun stopListen() = gpsDataSource.onStopListen()
    override fun requestCurrentLocation() = gpsDataSource.requestCurrentLocation()
    override  fun getLastKnownLocation(): Flow<Location?> {
        return flow{
            gpsDataSource.getLastKnownLocation()
        }
    }
    override fun setLocationListener(listener: GpsListener?) = gpsDataSource.setListener(listener)
    override fun hasProvider(): Boolean = gpsDataSource.hasProvider()
}
