/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut

class SettingsInteractorFake(
    results: List<Result<Settings>>,
) : InteractorContract.SettingsInteractor {
    private val results = results.toMutableList()

    private var _invoked: InvokedType = InvokedType.NONE
    val invoked: InvokedType
        get() = _invoked

    override val settings: Flow<Result<Settings>>
        get() = flow { results.forEach { emit(it) } }

    override fun insertOrIgnore(settings: Settings, scope: CoroutineScope) {
        _invoked = InvokedType.INSERT_OR_IGNORE
    }

    override fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope) {
        _invoked = InvokedType.SET_TIMEOUT
    }

    override fun setSnooze(snooze: Snooze, scope: CoroutineScope) {
        _invoked = InvokedType.SET_SNOOZE
    }

    override fun setTheme(theme: Theme, scope: CoroutineScope) {
        _invoked = InvokedType.SET_THEME
    }

    enum class InvokedType {
        SET_TIMEOUT,
        SET_SNOOZE,
        SET_THEME,
        INSERT_OR_IGNORE,
        NONE,
    }
}
