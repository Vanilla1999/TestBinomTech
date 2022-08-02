package com.example.test.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



data class UserPointModel(
    var id: Int = 0,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val img: String?,
    val description:String,
    val startWork:Long?,
    val endWork:Long?,
    val rating: Float?
)

