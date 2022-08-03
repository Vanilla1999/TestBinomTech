package com.example.test.domain

import com.example.test.data.model.UserPointModel
import com.example.test.data.repository.UserPointsRepo
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPointsUseCaseImpl @Inject constructor(private val userPointsRepo: UserPointsRepo):
    GetPointsUseCase {
    override suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel>> {
        return userPointsRepo.getUserPoints()
    }


}
interface GetPointsUseCase{
    suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel>>
}