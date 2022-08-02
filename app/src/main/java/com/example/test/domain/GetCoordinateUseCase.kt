package com.example.lesson_1.domain

import com.example.test.data.CoordinatesRepository
import com.example.test.data.model.UserLocationModel
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.Flow

class GetCoordinateUseCaseImpl(private val coordinatesRepository: CoordinatesRepository):GetCoordinateUseCase {
    override suspend fun getLastUserCoordinate(): Flow<ResponseDataBase<UserLocationModel>> {
        return coordinatesRepository.getLastCoordinate()
    }
}

interface GetCoordinateUseCase {
    suspend fun getLastUserCoordinate(): Flow<ResponseDataBase<UserLocationModel>>
}