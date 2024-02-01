package org.a_cyb.sayitalarm.core.alarm.util

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import org.a_cyb.sayitalarm.util.TAG

object SiaAsyncHandler {

    private val handlerThread = HandlerThread("SiaAsyncHandler")
    private val handler: Handler

    init {
        Log.d(TAG, "[***] init: SiaAsyncHandler")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    fun post(runnable: () -> Unit) {
        handler.post(runnable)
    }
}