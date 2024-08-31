/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Initial
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsUI
import org.a_cyb.sayitalarm.presentation.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.formatter.duration.DurationFormatterContract
import org.a_cyb.sayitalarm.presentation.link_opener.LinkOpenerContract

class SettingsViewModel(
    private val interactor: InteractorContract.SettingsInteractor,
    private val durationFormatter: DurationFormatterContract,
    private val linkOpenerContract: LinkOpenerContract,
) : SettingsContract.SettingsViewModel, ViewModel() {

    init {
        interactor.insertOrIgnore(
            getDefaultSettings(),
            scope
        )
    }

    override val state: StateFlow<SettingsState> = interactor.settings
        .map(::mapToState)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Initial
        )

    private fun mapToState(result: Result<Settings>): SettingsState = result
        .getOrNull()
        ?.toSuccess() ?: Error

    private fun Settings.toSuccess(): Success =
        Success(
            SettingsUI(
                timeOut = toTimeInput(timeOut.timeOut),
                snooze = toTimeInput(snooze.snooze),
                theme = theme.name.toCamelCase(),
            )
        )

    private fun toTimeInput(time: Int): TimeInput =
        TimeInput(
            input = time,
            formatted = durationFormatter.format(time.minutes).short
        )

    private fun String.toCamelCase(): String = lowercase().replaceFirstChar(Char::titlecase)

    override fun setTimeOut(timeOut: TimeOut) {
        interactor.setTimeOut(timeOut, scope)
    }

    override fun setSnooze(snooze: Snooze) {
        interactor.setSnooze(snooze, scope)
    }

    override fun setTheme(themeName: String) {
        val theme = Theme.entries.find { it.name == themeName.uppercase() } ?: Theme.LIGHT
        interactor.setTheme(theme, scope)
    }

    override fun sendEmail() {
        linkOpenerContract.openEmail(CONTACT_EMAIL, "")
    }

    override fun openGitHub() {
        linkOpenerContract.openBrowserLink(CONTACT_GITHUB)
    }

    override fun openGooglePlay() {
        linkOpenerContract.openGooglePlay()
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    override val timeOuts: List<TimeInput>
        get() = TIME_OUT_RANGE.step(10).map(::toTimeInput)

    override val snoozes: List<TimeInput>
        get() = SNOOZE_RANGE.step(5).map(::toTimeInput)

    override val themes: List<String>
        get() = Theme.entries.map { it.name.toCamelCase() }

    override val contact: SettingsContract.Contact
        get() = SettingsContract.Contact(
            CONTACT_EMAIL,
            CONTACT_GITHUB,
            CONTACT_GOOGLE_PLAY,
        )

    companion object {
        private const val CONTACT_EMAIL = "SayItAlarm@gmail.com"
        private const val CONTACT_GITHUB = "https://github.com/a-cyborg/SayItAlarm"
        private const val CONTACT_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=org.a_cyb.sayitalarm"

        private const val DEFAULT_TIMEOUT = 180
        private const val DEFAULT_SNOOZE = 15
        private val TIME_OUT_RANGE = 30..300
        private val SNOOZE_RANGE = 5..60

        fun getDefaultSettings(): Settings =
            Settings(
                TimeOut(DEFAULT_TIMEOUT),
                Snooze(DEFAULT_SNOOZE),
                Theme.LIGHT,
            )
    }
}
