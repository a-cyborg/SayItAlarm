/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut

class SettingsInteractor(
    private val settingsRepository: RepositoryContract.SettingsRepository
) : InteractorContract.SettingsInteractor {

    private val _settings: MutableSharedFlow<Result<Settings>> = MutableSharedFlow()
    override val settings: SharedFlow<Result<Settings>> = _settings

    override fun load(scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .getSettings()
                .map { it.emitResult() }
        }
    }

    private suspend fun Result<Settings>.emitResult() {
        this
            .onSuccess { _settings.emit(Result.success(it)) }
            .onFailure { _settings.emit(Result.failure(it)) }
    }

    override fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .setTimeOut(timeOut, this)

            load(this)
        }
    }

    override fun setSnooze(snooze: Snooze, scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .setSnooze(snooze, this)

            load(this)
        }
    }

    override fun setTheme(theme: Theme, scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .setTheme(theme, this)

            load(this)
        }
    }
}
