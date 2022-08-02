package com.example.test.data

import com.example.test.data.model.UserLocationModel
import com.example.test.data.model.UserPointModel
import com.example.test.data.model.convertToDto
import com.example.test.data.repository.BaseRepositoryDataBase
import com.example.test.data.source.database.DatabaseMain
import com.example.test.data.source.database.dto.convertToModel
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class CoordinatesRepositoryImpl(private val databaseSource: DatabaseMain) : CoordinatesRepository,
    BaseRepositoryDataBase() {
    override suspend fun getLastCoordinate(): Flow<ResponseDataBase<UserLocationModel>> {
        return databaseSource.userLocation().getLastLocation().transform {
            val convertData = it.convertToModel()
            doWorkNotList(convertData, this)
        }
    }

    override suspend fun insert(item: UserLocationModel) {
        databaseSource.userLocation().insertOrUpdate(item.convertToDto())
    }

    override suspend fun delete(list: List<UserLocationModel>) {
        databaseSource.userLocation().delete(list.map { it.convertToDto() })
    }
}

interface CoordinatesRepository {
    suspend fun getLastCoordinate(): Flow<ResponseDataBase<UserLocationModel>>

    suspend fun insert(item: UserLocationModel)

    suspend fun delete(list: List<UserLocationModel>)
}