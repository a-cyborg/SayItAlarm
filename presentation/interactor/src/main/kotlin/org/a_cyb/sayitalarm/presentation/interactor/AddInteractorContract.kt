/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.interactor

import org.a_cyb.sayitalarm.entity.Alarm

interface AddInteractorContract {
    suspend fun save(alarm: Alarm)
}