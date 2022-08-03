package com.example.test.data.repository

import com.example.test.data.model.UserLocationModel
import com.example.test.data.model.UserPointModel
import com.example.test.data.model.convertToDto
import com.example.test.data.source.database.DatabaseMain
import com.example.test.data.source.database.dto.UserPoint
import com.example.test.data.source.database.dto.convertToModel
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserPointsRepoImpl @Inject constructor(private val databaseSource: DatabaseMain) : UserPointsRepo,BaseRepositoryDataBase() {
    override suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel?>> {
        return databaseSource.points().getAllUserPoints().transform {
            val convertData = it.map { userPoint -> userPoint.convertToModel() }
            doWorkList(convertData, this)
        }
    }

    override suspend fun insert(item: UserPointModel) {
        databaseSource.points().insertOrUpdate(item.convertToDto())
    }

    override suspend fun delete(list: List<UserPointModel>) {
        databaseSource.points().delete(list.map { it.convertToDto() })
    }
}

interface UserPointsRepo {

    suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel?>>

    suspend fun insert(item: UserPointModel)

    suspend fun delete(list: List<UserPointModel>)
}