/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract.SettingsState
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract.SettingsViewModel
import org.a_cyb.sayitalarm.presentation.settings.SettingsContract.TimeInput

@Suppress("EmptyFunctionBlock")
class SettingsViewModelFake(
    initState: SettingsState = SettingsContract.SettingsStateWithContent(
        timeOut = TimeInput(180, formatted = "3 hr"),
        snooze = TimeInput(15, formatted = "15 min"),
        theme = "Light",
    ),
) : SettingsViewModel, ViewModel() {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(initState)
    override val state: StateFlow<SettingsState> = _state

    override val timeOuts: List<TimeInput> =
        (30..300).map { TimeInput(it, it.formatAsDuration()) }

    override val snoozes: List<TimeInput> =
        (5..60).map { TimeInput(it, it.formatAsDuration()) }

    override val themes: List<String> = listOf("Light", "Dark")
    override fun setTimeOut(timeOut: TimeOut) {}
    override fun setSnooze(snooze: Snooze) {}
    override fun setTheme(themeName: String) {}

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}

    private fun Int.formatAsDuration(): String {
        val hour = this / 60
        val min = this % 60

        return when {
            hour >= 1 && min > 1 -> "$hour hr $min min"
            hour >= 1 -> "$hour hr"
            min >= 1 -> "$min min"
            else -> "0 min"
        }
    }
}
