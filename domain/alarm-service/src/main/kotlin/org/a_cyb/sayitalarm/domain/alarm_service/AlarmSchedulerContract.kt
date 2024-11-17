/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.alarm_service

import org.a_cyb.sayitalarm.entity.Snooze

interface AlarmSchedulerContract {
    fun scheduleAlarms()
    fun scheduleSnooze(alarmId: Long, snooze: Snooze)
    fun cancelAlarm(alarmId: Long)
}
