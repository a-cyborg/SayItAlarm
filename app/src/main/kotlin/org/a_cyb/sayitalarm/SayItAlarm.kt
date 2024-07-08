package org.a_cyb.sayitalarm

import android.app.Application
import org.a_cyb.sayitalarm.di.initKoin

class SayItAlarm : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(this)
    }
}
