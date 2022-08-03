package com.example.test.data.repository.mockRepo

import com.example.test.data.model.UserLocationModel
import com.example.test.data.model.UserPointModel
import com.example.test.data.model.convertToDto
import com.example.test.data.repository.BaseRepositoryDataBase
import com.example.test.data.repository.UserPointsRepo
import com.example.test.data.source.database.DatabaseMain
import com.example.test.data.source.database.dto.UserPoint
import com.example.test.data.source.database.dto.convertToModel
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class UserPointsRepoMockImpl @Inject constructor() : UserPointsRepo, BaseRepositoryDataBase() {
    private val mutableList = mutableListOf<UserPointModel>()

    init {
        mutableList.add(
            UserPointModel(
                id = 2, name = "Виталий",
                latitude = 45.01436,
                longitude = 38.96696,
                img = "1",
                time = 8 * 60 * 60 * 1000L,
                date = Date(),
                coordinateProvider = "GPS"
            )
        )
        mutableList.add(
            UserPointModel(
                id = 3, name = "Олег",
                latitude = 45.03215,
                longitude = 39.02482,
                img = "2",
                time = 8 * 60 * 60 * 1000L,
                date = Date(),
                coordinateProvider = "WIFI"
            )
        )
        mutableList.add(
            UserPointModel(
                id = 4, name = "Виталий",
                latitude = 45.04442,
                longitude = 39.0293,
                img = "3",
                time = 8 * 60 * 60 * 1000L,
                date = Date(),
                coordinateProvider = "GPS"
            )
        )

    }

    override suspend fun getUserPoints(): Flow<ResponseDataBase<UserPointModel>> {
        return flow {
            doWorkList(mutableList, this)
        }
    }

    override suspend fun insert(item: UserPointModel) {
        mutableList.add(item)
    }

    override suspend fun delete(list: List<UserPointModel>) {
        list.forEach { element ->
            mutableList.remove(element)
        }
    }
}

