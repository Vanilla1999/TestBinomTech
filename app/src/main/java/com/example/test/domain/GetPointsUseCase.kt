package com.example.lesson_1.domain

import androidx.lifecycle.LiveData
import com.example.test.data.CoordinatesRepository
import com.example.test.data.model.UserLocationModel
import com.example.test.data.model.UserPointModel
import com.example.test.data.repository.UserPointsRepo
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.Flow

class GetPointsUseCaseImpl (private val userPointsRepo: UserPointsRepo):GetPointsUseCase{
    override suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel>> {
        return userPointsRepo.getUserPoints()
    }


}
interface GetPointsUseCase{
    suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel>>
}