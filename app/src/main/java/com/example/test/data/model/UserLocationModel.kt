package com.example.test.data.model

import androidx.room.Entity
import com.example.test.data.source.database.dto.UserLocation
import com.example.test.data.source.database.dto.UserPoint

@Entity(
    tableName = "user_location",
    primaryKeys = ["time"]
)
data class UserLocationModel(
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val provider: String,
    val accuracy: Float,
    val bearing: Float,
    val hasBearingMask:Boolean
)
fun UserLocationModel.convertToDto(): UserLocation{
    return UserLocation(time, latitude, longitude, provider, accuracy, bearing, hasBearingMask)
}