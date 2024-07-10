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
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.entity.Alarm

class EditInteractorFake(
    private val result: Result<Alarm>
) : InteractorContract.EditInteractor {

    private val _alarm = MutableSharedFlow<Result<Alarm>>()
    override val alarm: SharedFlow<Result<Alarm>> = _alarm

    private var _invoked: InvokedType = InvokedType.NONE
    val invoked: InvokedType
        get() = _invoked

    private var _updatedAlarm: Alarm? = null
    val updatedAlarm: Alarm?
        get() = _updatedAlarm

    override fun getAlarm(id: Long, scope: CoroutineScope) {
        scope.launch {
            _alarm.emit(result)
        }

        _invoked = InvokedType.GET_ALARM
    }

    override fun update(alarm: Alarm, scope: CoroutineScope) {
        _invoked = InvokedType.UPDATE

        _updatedAlarm = alarm
    }

    enum class InvokedType {
        GET_ALARM,
        UPDATE,
        NONE,
    }
}
