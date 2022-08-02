package com.example.test

import android.app.Application
import com.example.test.di.ApplicationComponent
import com.example.test.di.DaggerApplicationComponent


class App() : Application() {

    override fun onCreate() {
        appComponentMain = DaggerApplicationComponent.factory().create(this)
        super.onCreate()
    }
    companion object {
        lateinit var appComponentMain: ApplicationComponent
    }
}