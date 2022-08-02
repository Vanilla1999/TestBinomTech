package com.example.test

import android.app.Application


class App() : Application() {

    override fun onCreate() {
        appComponentMain = DaggerApplicationComponent.factory().create(this)
        super.onCreate()
    }
    companion object {
        lateinit var appComponentMain: ApplicationComponent
    }
}