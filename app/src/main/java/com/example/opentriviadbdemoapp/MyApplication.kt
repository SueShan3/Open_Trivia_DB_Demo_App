package com.example.opentriviadbdemoapp

import android.app.Application
import com.example.opentriviadbdemoapp.module.networkModule
import com.example.opentriviadbdemoapp.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf( networkModule, viewModelModule))
        }
    }
}