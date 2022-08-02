package com.example.test.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.test.data.dto.UserLocation
import com.example.test.data.dto.UserPoint
import com.example.test.data.source.database.conventors.DBConvertor
import com.example.test.data.source.database.dao.UserLocationDao
import com.example.test.data.source.database.dao.UserPointDao


@Database(
    version = 1,
    entities = [
        UserLocation::class,
        UserPoint::class,
    ],
    autoMigrations = [
    ],
    exportSchema = true
)
@TypeConverters(DBConvertor.PrimitiveTypeConvertor::class)
abstract class DatabaseMain : RoomDatabase() {
    abstract fun userLocation(): UserLocationDao
    abstract fun points(): UserPointDao

    companion object {
        val migrations: Array<Migration> = arrayOf(
        )
        const val DATABASE = "TestBinom"
    }
}