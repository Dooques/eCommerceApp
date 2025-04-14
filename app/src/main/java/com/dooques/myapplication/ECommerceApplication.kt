package com.dooques.myapplication

import android.app.Application
import com.dooques.myapplication.di.networkModule
import com.dooques.myapplication.di.repositoryModule
import com.dooques.myapplication.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ECommerceApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ECommerceApplication)
            modules(networkModule, repositoryModule, viewModelModule)
            androidLogger()
        }
    }
}