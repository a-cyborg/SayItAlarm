package org.a_cyb.sayitalarm.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.core.data.repository.UserDataRepository
import org.a_cyb.sayitalarm.core.model.AlarmVolumeButtonBehavior
import org.a_cyb.sayitalarm.core.model.DarkThemeConfig
import org.a_cyb.sayitalarm.core.model.UserData
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userData
            .map(SettingsUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState.Loading
            )

    fun updateAlarmTimeOut(min: Int) {
        viewModelScope.launch {
            userDataRepository.setTimeOut(min)
        }
    }

    fun updateSnoozeLength(min: Int) {
        viewModelScope.launch {
            userDataRepository.setSnoozeLength(min)
        }
    }

    fun updateAlarmCrescendoDuration(min: Int) {
        viewModelScope.launch {
            userDataRepository.setRingtoneCrescendoDuration(min)
        }
    }

    fun updateAlarmVolumeButtonBehavior(volumeButtonBehavior: AlarmVolumeButtonBehavior) {
        viewModelScope.launch {
            userDataRepository.setVolumeButtonBehavior(volumeButtonBehavior)
        }
    }

    fun updateDarkThemConfig(darkTheme: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkTheme)
        }
    }
}

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val userData: UserData) : SettingsUiState
}