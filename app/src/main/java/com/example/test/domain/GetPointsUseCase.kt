package com.example.lesson_1.domain

import androidx.lifecycle.LiveData
import com.example.test.data.CoordinatesRepository
import com.example.test.data.model.UserPointModel
import kotlinx.coroutines.flow.Flow

class GetPointsUseCaseImpl (private val coordinatesRepository: CoordinatesRepository):GetPointsUseCase{
    override suspend fun getUserPoints(): Flow<List<UserPointModel>> {
        TODO("Not yet implemented")
    }


}
interface GetPointsUseCase{
    suspend fun getUserPoints(): Flow<List<UserPointModel>>
}