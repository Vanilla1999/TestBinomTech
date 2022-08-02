package com.example.test.data.repository

import com.example.test.data.dto.UserPoint
import com.example.test.data.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

class UserPointsRepoImpl:UserPointsRepo {
    override suspend fun getUserPoints(): Flow<List<UserLocationModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(item: UserPoint) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(list: List<UserPoint>) {
        TODO("Not yet implemented")
    }
}
interface UserPointsRepo{

    suspend fun getUserPoints():Flow<List<UserLocationModel>>

    suspend fun insert(item: UserPoint)

    suspend fun delete(list: List<UserPoint>)
}