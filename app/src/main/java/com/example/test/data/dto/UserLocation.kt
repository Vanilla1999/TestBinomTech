package com.example.test.data.dto

import androidx.room.Entity

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
