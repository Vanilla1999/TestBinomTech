package com.example.test.di

import android.content.Context
import androidx.room.Room
import com.example.test.data.repository.CoordinatesRepository
import com.example.test.data.repository.CoordinatesRepositoryImpl
import com.example.test.data.source.database.DatabaseMain
import com.example.test.data.source.gps.GpsDataSource
import com.example.test.data.source.gps.GpsDataSourceImpl
import com.example.test.data.source.hardware.GpsRepository
import com.example.test.data.source.hardware.GpsRepositoryImpl
import dagger.*


@Module()
class ApplicationModule {

    @Provides
    fun getRoomDatabase(
        @ApplicationContext
        context: Context
    ): DatabaseMain {
        val builder = Room.databaseBuilder(
            context, DatabaseMain::class.java, DatabaseMain.DATABASE
        )
        builder.addMigrations(*DatabaseMain.migrations)
        return builder.build()
    }
}

@Module
interface LocationModule {
    @Binds
    @Suppress("FunctionName")
    fun bindsCoordinatesRepository_to_CoordinatesRepositoryImpl(coordinatesRepositoryImpl: CoordinatesRepositoryImpl): CoordinatesRepository

    @Binds
    fun bindsGpsDataSource_to_GpsDataSourceImpl(gpsDataSourceImpl: GpsDataSourceImpl): GpsDataSource

    @Binds
    fun bindsGpsRepo_to_GpsRepoImpl(gpsRepositoryImpl: GpsRepositoryImpl): GpsRepository
}

@Component(modules = [ApplicationModule::class, LocationModule::class])
interface ApplicationComponent {

    fun getRoomDatabase(): DatabaseMain

    @Suppress("FunctionName")
    fun bindsGpsDataSource_to_GpsDataSourceImpl(): GpsRepository

    @Suppress("FunctionName")
    fun bindsGpsRepo_to_GpsRepoImpl(): GpsDataSource

    @Suppress("FunctionName")
    fun bindsCoordinatesRepository_to_CoordinatesRepositoryImpl(): CoordinatesRepository

    @ApplicationContext
    fun getApplication(): Context

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @ApplicationContext context: Context): ApplicationComponent
    }
}
