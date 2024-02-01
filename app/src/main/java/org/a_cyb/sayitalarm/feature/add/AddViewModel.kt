package org.a_cyb.sayitalarm.feature.add

import android.media.RingtoneManager
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.core.alarm.NoOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.data.repository.AlarmRepository
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    private val _addUiState = MutableStateFlow(AddUiSate())
    val addUiState: StateFlow<AddUiSate> = _addUiState.asStateFlow()

    fun updateTime(hourOfDay: Int, minute: Int) = _addUiState.update { currentState ->
        // TODO: Check add with time =
        currentState.copy(CombinedMinutes(hourOfDay, minute))
    }

    fun updateWeeklyRepeat(days: Set<Int>) = _addUiState.update { currentState ->
            currentState.copy(weeklyRepeat = WeeklyRepeat(days))
        }

    fun updateLabel(label: String) = _addUiState.update { currentState ->
        currentState.copy(label = label)
    }

    fun updateRingtone(ringtone: Uri) = _addUiState.update { currentState ->
        currentState.copy(ringtone = ringtone)
    }

    fun updateSayItText(text: List<String>) = _addUiState.update { currentState ->
        currentState.copy(sayItText = text)
    }

    fun saveAlarm() {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.insertAlarm(
                Alarm(
                    combinedMinutes = addUiState.value.time,
                    enabled = true,
                    weeklyRepeat = _addUiState.value.weeklyRepeat,
                    label = _addUiState.value.label,
                    vibrate = false, // Not implemented yet.
                    ringtone = _addUiState.value.ringtone,
                    // TODO: Before saving the alarm implements available test for the SayItText.
                    alarmTerminator = VoiceRecognitionTerminator(_addUiState.value.sayItText),
                    alarmOptionalFeature = NoOptionalFeature, // Not implemented yet.
                )
            )
        }
    }
}

data class AddUiSate (
    val time: CombinedMinutes = CombinedMinutes(8, 30),
    val weeklyRepeat: WeeklyRepeat = WeeklyRepeat.NEVER,
    val label: String = "",
    val ringtone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
    val sayItText: List<String> = listOf(""),
)

// TODO
//sealed interface AddUiState {
//    data object Loading: AddUiState
//    data class Success(val alarmFieldValues: AlarmFieldValues): AddUiState
//}


//    fun updateAlarmTime(hourOfDay: Int, minute: Int) = _addUiState.update { currentState ->
//            currentState.copy(alarmTime = Calendar.getInstance().apply {
//                    set(Calendar.HOUR_OF_DAY, hourOfDay)
//                    set(Calendar.MINUTE, minute)
//                }
//            )
//        }
