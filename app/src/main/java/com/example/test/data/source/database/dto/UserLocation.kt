package com.example.test.data.source.database.dto

import androidx.room.Entity
import com.example.test.data.model.UserLocationModel
import com.example.test.data.model.UserPointModel

@Entity(
    tableName = "user_location",
    primaryKeys = ["time"]
)
data class UserLocation(
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val provider: String,
    val accuracy: Float,
    val bearing: Float,
    val hasBearingMask:Boolean
)
fun UserLocation?.convertToModel(): UserLocationModel? {
    return this?.let { return UserLocationModel(time, latitude, longitude, provider, accuracy, bearing, hasBearingMask) } ?: return null
}