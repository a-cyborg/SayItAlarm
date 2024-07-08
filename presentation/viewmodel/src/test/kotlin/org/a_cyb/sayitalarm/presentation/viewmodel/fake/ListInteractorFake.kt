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
import org.a_cyb.sayitalarm.entity.Alarm

class ListInteractorFake(
    results: List<Result<List<Alarm>>> = listOf(Result.failure(IllegalStateException())),
) : InteractorContract.ListInteractor {
    private val results = results.toMutableList()

    private var _invokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    override val alarms: Flow<Result<List<Alarm>>>
        get() = flow { results.forEach { emit(it) } }

    override fun setEnabled(id: Long, enabled: Boolean, scope: CoroutineScope) {
        _invokedType = InvokedType.SET_ENABLED
    }

    override fun deleteAlarm(id: Long, scope: CoroutineScope) {
        _invokedType = InvokedType.DELETE_ALARM
    }

    enum class InvokedType {
        SET_ENABLED,
        DELETE_ALARM,
        NONE,
    }
}
