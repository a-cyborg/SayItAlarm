package org.a_cyb.sayitalarm

import android.app.Application
import org.a_cyb.sayitalarm.di.initKoin
import org.koin.core.context.stopKoin

class SayItAlarm : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(this)
    }

    override fun onTerminate() {
        super.onTerminate()

        stopKoin()
    }
}
