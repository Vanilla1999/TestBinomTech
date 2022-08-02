package com.example.test.data

class CoordinatesRepositoryImpl:CoordinatesRepository {
}
interface CoordinatesRepository {
    suspend fun getLastCoordinate():
}