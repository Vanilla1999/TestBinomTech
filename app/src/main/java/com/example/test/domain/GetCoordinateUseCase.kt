package com.example.lesson_1.domain

import com.example.test.data.CoordinatesRepository
import com.example.test.data.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

class GetCoordinateUseCaseImpl(private val coordinatesRepository: CoordinatesRepository) {
    suspend fun getLastUserCoordinate(): Flow<UserLocationModel> {
        return coordinatesRepository.getLastCoordinate()
    }
}

interface GetCoordinateUseCase {
    suspend fun getLastUserCoordinate(): Flow<UserLocationModel>
}