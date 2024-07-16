package org.a_cyb.sayitalarm

import android.app.Application
import org.a_cyb.sayitalarm.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SayItAlarm : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SayItAlarm)
            modules(appModule)
        }
    }
}
