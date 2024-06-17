/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

class SettingsInteractorFake(
    results: List<Result<Settings>>,
    scope: CoroutineScope,
) : InteractorContract.SettingsInteractor {
    private val results = results.toMutableList()

    private val _settings: MutableSharedFlow<Result<Settings>> = MutableSharedFlow()
    override val settings: SharedFlow<Result<Settings>> = _settings

    private var _invoked: InvokedType = InvokedType.NONE
    val invoked: InvokedType
        get() = _invoked

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

        _invoked = InvokedType.SET_TIMEOUT
    }

    override fun setSnooze(snooze: Snooze, scope: CoroutineScope) {
        scope.launch {
            _settings.emit(results.removeFirst())
        }

        _invoked = InvokedType.SET_SNOOZE
    }

    override fun setTheme(theme: Theme, scope: CoroutineScope) {
        scope.launch {
            _settings.emit(results.removeFirst())
        }

        _invoked = InvokedType.SET_THEME
    }

    enum class InvokedType {
        SET_TIMEOUT,
        SET_SNOOZE,
        SET_THEME,
        NONE,
    }
}
