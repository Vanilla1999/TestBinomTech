package com.example.lesson_1.domain

import com.example.test.data.repository.CoordinatesRepository
import com.example.test.data.model.UserLocationModel
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoordinateUseCaseImpl @Inject constructor(private val coordinatesRepository: CoordinatesRepository):GetCoordinateUseCase {
    override suspend fun getLastUserCoordinate(): Flow<ResponseDataBase<UserLocationModel>> {
        return coordinatesRepository.getLastCoordinate()
    }
}

interface GetCoordinateUseCase {
    suspend fun getLastUserCoordinate(): Flow<ResponseDataBase<UserLocationModel>>
}