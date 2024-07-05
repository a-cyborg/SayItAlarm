/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.mapper

import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlarmUI

interface AlarmMapperContract {
    fun mapToAlarm(alarmUI: AlarmUI): Alarm
    fun mapToAlarmUI(alarm: Alarm): AlarmUI
}
