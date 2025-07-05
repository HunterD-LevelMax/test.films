package com.euphoria.app

import android.app.Application
import com.euphoria.app.di.networkModule
import com.euphoria.app.di.repositoryModule
import com.euphoria.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}