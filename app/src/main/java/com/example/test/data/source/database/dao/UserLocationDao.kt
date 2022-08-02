package com.example.test.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.test.data.dto.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserLocationDao : BaseDao<UserLocation>() {

    @Query("SELECT * FROM user_location ORDER BY time DESC LIMIT 1   ")
    abstract fun getLastLocation(): Flow<UserLocation>

    @Query("SELECT * FROM user_location")
    abstract fun getAllUserLocation(): Flow<List<UserLocation>>
}