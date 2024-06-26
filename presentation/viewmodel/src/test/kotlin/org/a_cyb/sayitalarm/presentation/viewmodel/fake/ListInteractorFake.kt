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
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

class ListInteractorFake(
    scope: CoroutineScope,
    results: List<Result<List<Alarm>>> = listOf(Result.failure(IllegalStateException())),
) : InteractorContract.ListInteractor {
    private val results = results.toMutableList()

    private val _alarm: MutableSharedFlow<Result<List<Alarm>>> = MutableSharedFlow()
    override val alarms: SharedFlow<Result<List<Alarm>>> = _alarm

    private var _invokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    init {
        scope.launch { load(scope) }
    }

    override fun load(scope: CoroutineScope) {
        scope.launch {
            _alarm.emit(results.removeFirst())
        }
    }

    override fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope) {
        scope.launch {
            _alarm.emit(results.removeFirst())
        }

        _invokedType = InvokedType.SET_ENABLED
    }

    override fun deleteAlarm(id: Long, scope: CoroutineScope) {
        scope.launch {
            _alarm.emit(results.removeFirst())
        }

        _invokedType = InvokedType.DELETE_ALARM
    }

    enum class InvokedType {
        SET_ENABLED,
        DELETE_ALARM,
        NONE,
    }
}
