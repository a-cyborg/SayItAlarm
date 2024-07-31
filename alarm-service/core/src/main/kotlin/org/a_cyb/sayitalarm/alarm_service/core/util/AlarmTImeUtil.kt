/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.WeeklyRepeat

fun getNextAlarmTimeInMills(hour: Hour, minute: Minute, weeklyRepeat: WeeklyRepeat): Long {
    val alarmLocalDateTime = getNextAlarmTime(
        LocalTime.of(hour.hour, minute.minute),
        weeklyRepeat
    )

    return ZonedDateTime
        .of(alarmLocalDateTime, ZoneId.systemDefault())
        .toEpochSecond() * 1000
}

fun getNextAlarmTime(alarmTime: LocalTime, weeklyRepeat: WeeklyRepeat): LocalDateTime {
    val alarmDate = getNextDayOfAlarm(alarmTime, weeklyRepeat)

    return LocalDateTime.of(alarmDate, alarmTime)
        .withSecond(0)
        .withNano(0)
}

private fun getNextDayOfAlarm(alarmTime: LocalTime, weeklyRepeat: WeeklyRepeat): LocalDate {
    val nowDate = LocalDate.now()
    val nowTime = LocalTime.now()
    val todayCode = nowDate.dayOfWeek.value

    // The alarm time for today is still upcoming.
    if (alarmTime.isAfter(nowTime) &&
        (weeklyRepeat.weekdays.contains(todayCode) || !weeklyRepeat.isRepeat())
    ) {
        return LocalDate.now()
    }

    // The alarm time for today has already passed.
    return if (!weeklyRepeat.isRepeat()) {
        nowDate.plusDays(1)
    } else {
        // If no day is available this week, select the first available date next week.
        val nextDayCode = weeklyRepeat.weekdays
            .firstOrNull { it > todayCode }
            ?: weeklyRepeat.weekdays.first()

        nowDate
            .with(TemporalAdjusters.next(DayOfWeek.of(nextDayCode)))
    }
}

private fun WeeklyRepeat.isRepeat(): Boolean =
    weekdays.isNotEmpty()
