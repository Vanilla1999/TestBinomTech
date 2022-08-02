package com.example.test.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.test.data.source.database.dto.UserPoint
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserPointDao : BaseDao<UserPoint>() {

    @Query("SELECT * FROM userPoints")
    abstract fun getAllUserPoints(): Flow<List<UserPoint>>

}