/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.tasker.SettingsTaskerContract

class SettingsTaskerFake(
    results: List<Result<Settings>>,
    scope: CoroutineScope,
) : SettingsTaskerContract {

    private val results = results.toMutableList()

    private val _settings: MutableSharedFlow<Result<Settings>> = MutableSharedFlow()
    override val settings: SharedFlow<Result<Settings>> = _settings

    init {
        scope.launch { load(this) }
    }

    override fun load(scope: CoroutineScope) {
        scope.launch {
            _settings.emit(results.removeFirst())
        }
    }

    override fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope) {
        scope.launch {
            _settings.emit(results.removeFirst())
        }
    }

    override fun setSnooze(snooze: Snooze, scope: CoroutineScope) {
        scope.launch {
            _settings.emit(results.removeFirst())
        }
    }

    override fun setTheme(theme: Theme, scope: CoroutineScope) {
        scope.launch {
            _settings.emit(results.removeFirst())
        }
    }
}
