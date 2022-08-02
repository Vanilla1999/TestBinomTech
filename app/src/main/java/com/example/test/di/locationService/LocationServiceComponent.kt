package com.example.test.di.locationService

import com.example.test.di.ApplicationComponent
import com.example.test.di.LocationServiceScope
import com.example.test.services.LocationService
import dagger.Component
import dagger.Module

@Module
interface  MainModule {
}

@LocationServiceScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModule::class]
)
interface LocationServiceComponent {
    fun inject(fragment: LocationService)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): LocationServiceComponent
    }
}