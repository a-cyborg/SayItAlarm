package org.a_cyb.sayitalarm.feature.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.data.repository.AlarmRepository
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    private val alarmId: Int = (savedStateHandle["alarmId"] ?: Alarm.INVALID_ID).toInt()

    val editUiState: StateFlow<EditUiState> =
        alarmRepository.getAlarmById(alarmId)
            .map {
                when (it) {
                    null -> EditUiState.Error
                    else -> EditUiState.Success(it, MutableStateFlow(it))
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = EditUiState.Loading
            )

    fun onUpdateCombinedMinutes(hourOfDay: Int, minute: Int) =
        editedAlarmState.update { currentState ->
            currentState.copy(combinedMinutes = CombinedMinutes(hourOfDay, minute))
        }

    fun onUpdateWeeklyRepeat(days: Set<Int>) = editedAlarmState.update { currentState ->
        currentState.copy(weeklyRepeat = WeeklyRepeat(days))
    }

    fun onUpdateLabel(label: String) = editedAlarmState.update { currentState ->
            currentState.copy(label = label)
        }

    fun onUpdateRingtone(ringtone: Uri) = editedAlarmState.update { currentState ->
            currentState.copy(ringtone = ringtone)
        }

    fun onUpdateSayItText(text: List<String>) = editedAlarmState.update { currentState ->
        currentState.copy(alarmTerminator = VoiceRecognitionTerminator(text))
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.insertAlarm(editedAlarmState.value)
        }
    }

    fun onDeleteAlarm() {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.deleteAlarm(
                successUiState.originalAlarm.id.toInt()
            )
        }
    }

    fun onCancel() {
        viewModelScope.launch(Dispatchers.IO) {
            // Restore original version of alarm.
            alarmRepository.insertAlarm(successUiState.originalAlarm)
        }
    }

    /**
     * Get edited alarm value.
     */
    private val successUiState: EditUiState.Success
        get() = (editUiState.value as EditUiState.Success)

    /**
     * Get mutableStateFlow of editedAlarm
     */
    private val editedAlarmState: MutableStateFlow<Alarm>
        get() = (editUiState.value as EditUiState.Success).editedAlarm
}

sealed interface EditUiState {
    data object Loading : EditUiState
    data class Success(val originalAlarm: Alarm, val editedAlarm: MutableStateFlow<Alarm>) : EditUiState
    data object Error : EditUiState
}