package com.example.test.data

import com.example.test.data.dto.UserPoint
import com.example.test.data.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

class CoordinatesRepositoryImpl:CoordinatesRepository {
    override suspend fun getLastCoordinate(): Flow<UserLocationModel> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(item: UserLocationModel) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(list: List<UserLocationModel>) {
        TODO("Not yet implemented")
    }
}
interface CoordinatesRepository {
    suspend fun getLastCoordinate(): Flow<UserLocationModel>

    suspend fun insert(item: UserLocationModel)

    suspend fun delete(list: List<UserLocationModel>)
}