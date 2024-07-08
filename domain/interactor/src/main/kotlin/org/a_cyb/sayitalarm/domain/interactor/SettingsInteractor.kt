/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut

class SettingsInteractor(
    private val settingsRepository: RepositoryContract.SettingsRepository
) : InteractorContract.SettingsInteractor {

    override val settings: Flow<Result<Settings>>
        get() = settingsRepository.getSettings()

    override fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .setTimeOut(timeOut, this)
        }
    }

    override fun setSnooze(snooze: Snooze, scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .setSnooze(snooze, this)
        }
    }

    override fun setTheme(theme: Theme, scope: CoroutineScope) {
        scope.launch {
            settingsRepository
                .setTheme(theme, this)
        }
    }
}
