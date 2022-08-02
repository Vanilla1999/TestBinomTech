package com.example.test.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(
    tableName = "userPoints",
)
data class UserPoint(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val img: String?,
    val time:Long?,
    val date: Date?,
    @ColumnInfo(defaultValue = "0.0")
    val coordinateProvider: String?
)

