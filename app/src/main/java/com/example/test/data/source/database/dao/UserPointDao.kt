package com.example.test.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.test.data.dto.UserPoint
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserPointDao : BaseDao<UserPointDao>() {

    @Query("SELECT * FROM userPoints ORDER BY rating DESC")
    abstract fun getAllMarksers(): Flow<List<UserPoint>>

}