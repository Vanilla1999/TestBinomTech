package com.example.test.data.model

import com.example.test.data.source.database.dto.UserPoint
import java.util.*


data class UserPointModel(
    var id: Int = 0,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val img: String?,
    val time:Long?,
    val date: Date?,
    val coordinateProvider: String?
)
fun UserPointModel.convertToDto():UserPoint{
    return UserPoint(id,name, latitude, longitude, img, time, date, coordinateProvider)
}
