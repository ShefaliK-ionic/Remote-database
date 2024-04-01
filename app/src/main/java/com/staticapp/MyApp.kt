package com.staticapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp :Application() {

    override fun onCreate() {
        super.onCreate()
        MyApp.appContext = applicationContext
    }

    companion object {

        lateinit  var appContext: Context

    }
}