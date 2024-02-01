package org.a_cyb.sayitalarm.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.core.data.repository.AlarmRepository
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    val listUiState: StateFlow<ListUiState> =
        alarmRepository.getAlarmSortedByTime()
            .map { alarms ->
                ListUiState.Success(
                    alarms.map {
                        AlarmListItem(
                            id = it.id,
                            time = it.combinedMinutes,
                            label = it.label,
                            weeklyRepeat = it.weeklyRepeat,
                            enabled = it.enabled,
                        )
                    }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ListUiState.Loading
            )

    fun updateAlarmEnabledState(id: Int, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.updateAlarm(id, enabled)
        }
    }

    fun deleteAlarm(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.deleteAlarm(id)
        }
    }
}

data class AlarmListItem(
    val id: Int,
    val time: CombinedMinutes,
    val label: String,
    val weeklyRepeat: WeeklyRepeat,
    val enabled: Boolean,
)

sealed interface ListUiState {
    data object Loading : ListUiState
    data class Success(val alarms: List<AlarmListItem>) : ListUiState
    data object Error : ListUiState
}