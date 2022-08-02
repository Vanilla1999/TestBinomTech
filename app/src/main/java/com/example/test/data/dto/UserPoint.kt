package com.example.test.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "userPoints",
)
data class UserPoint(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val img: ByteArray?,
    @ColumnInfo(defaultValue = "нет информации")
    val description:String,
    val startWork:Long?,
    val endWork:Long?,
    @ColumnInfo(defaultValue = "0.0")
    val rating: Float?
)

