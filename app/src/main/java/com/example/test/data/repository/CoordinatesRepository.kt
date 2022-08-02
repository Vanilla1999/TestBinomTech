package com.example.test.data

import com.example.test.data.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

class CoordinatesRepositoryImpl:CoordinatesRepository {
    override suspend fun getLastCoordinate(): Flow<UserLocationModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserPoints(): Flow<List<UserLocationModel>> {
        TODO("Not yet implemented")
    }
}
interface CoordinatesRepository {
    suspend fun getLastCoordinate(): Flow<UserLocationModel>
    suspend fun getUserPoints():Flow<List<UserLocationModel>>
}