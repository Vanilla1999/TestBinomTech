package com.example.test.di.mainActivtiy

import com.example.test.domain.GetCoordinateUseCase
import com.example.test.domain.GetCoordinateUseCaseImpl
import com.example.test.domain.GetPointsUseCase
import com.example.test.domain.GetPointsUseCaseImpl
import com.example.test.data.repository.UserPointsRepo
import com.example.test.data.repository.UserPointsRepoImpl
import com.example.test.data.repository.mockRepo.UserPointsRepoMockImpl
import com.example.test.di.ApplicationComponent
import com.example.test.di.MainActivityScope
import com.example.test.presentation.MainActivity
import dagger.Binds
import dagger.Component
import dagger.Module


@Module
interface MainModule {

    @Binds
    @Suppress("FunctionName")
    fun bindsUserPointRepository_to_UserPointRepositoryImpl(userPointsRepoImpl: UserPointsRepoImpl): UserPointsRepo

    @Binds
    @Suppress("FunctionName")
    fun bindsGetCoordinatesUseCase_to_GetCoordinatesUseCaseImpl(getCoordinateUseCaseImpl: GetCoordinateUseCaseImpl): GetCoordinateUseCase
    @Binds
    @Suppress("FunctionName")
    fun bindsGetUserPointsUseCase_to_GetUserPointsUseCaseImpl(getPointsUseCaseImpl: GetPointsUseCaseImpl): GetPointsUseCase
}



@MainActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModule::class]
)
interface MainActvitityComponent {



    @Suppress("FunctionName")
    fun bindsUserPointRepository_to_UserPointRepositoryImpl(): UserPointsRepo

    @Suppress("FunctionName")
    fun bindsGetCoordinatesUseCase_to_GetCoordinatesUseCaseImpl(): GetCoordinateUseCase

    @Suppress("FunctionName")
    fun bindsGetUserPointsUseCase_to_GetUserPointsUseCaseImpl(): GetPointsUseCase


    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): MainActvitityComponent
    }
}

@Module
interface MainModuleTest {

    @Binds
    @Suppress("FunctionName")
    fun bindsUserPointRepository_to_UserPointRepositoryImpl(userPointsRepoImpl: UserPointsRepoMockImpl): UserPointsRepo

    @Binds
    @Suppress("FunctionName")
    fun bindsGetCoordinatesUseCase_to_GetCoordinatesUseCaseImpl(getCoordinateUseCaseImpl: GetCoordinateUseCaseImpl): GetCoordinateUseCase
    @Binds
    @Suppress("FunctionName")
    fun bindsGetUserPointsUseCase_to_GetUserPointsUseCaseImpl(getPointsUseCaseImpl: GetPointsUseCaseImpl): GetPointsUseCase
}



@MainActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModuleTest::class]
)
interface MainActvitityComponentTest {



    @Suppress("FunctionName")
    fun bindsUserPointRepository_to_UserPointRepositoryImpl(): UserPointsRepo

    @Suppress("FunctionName")
    fun bindsGetCoordinatesUseCase_to_GetCoordinatesUseCaseImpl(): GetCoordinateUseCase

    @Suppress("FunctionName")
    fun bindsGetUserPointsUseCase_to_GetUserPointsUseCaseImpl(): GetPointsUseCase


    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): MainActvitityComponentTest
    }
}
