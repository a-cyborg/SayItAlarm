/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import kotlinx.coroutines.CoroutineScope
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

class EditInteractorFake(
    private val result: Result<Alarm>
) : InteractorContract.EditInteractor {

    private var _invoked: InvokedType = InvokedType.NONE
    val invoked: InvokedType
        get() = _invoked

    private var _updatedAlarm: Alarm? = null
    val updatedAlarm: Alarm?
        get() = _updatedAlarm

    override fun getAlarm(id: Long, scope: CoroutineScope): Result<Alarm> {
        _invoked = InvokedType.GET_ALARM

        return result
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
