package org.a_cyb.sayitalarm.feature.alarm

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.a_cyb.sayitalarm.core.alarm.AlarmStateManager
import org.a_cyb.sayitalarm.core.data.repository.AlarmRepository
import org.a_cyb.sayitalarm.util.TAG
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    fun onSayItButtonClicked() {
        Log.d(TAG, "onSayItButtonClicked: [***] onSayItButtonClicked")
        AlarmStateManager.changeSate(3)
    }

    fun onSnoozeButtonClicked() {}
    fun onStartSayIt() {}

    fun onClose() {
        exitProcess(0);
    }
}