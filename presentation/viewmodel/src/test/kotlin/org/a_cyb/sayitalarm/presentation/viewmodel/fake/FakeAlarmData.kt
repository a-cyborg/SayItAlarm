/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.fake

import android.icu.util.Calendar
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableRepeat
import java.util.Calendar.FRIDAY
import java.util.Calendar.MONDAY
import java.util.Calendar.SATURDAY
import java.util.Calendar.SUNDAY
import java.util.Calendar.THURSDAY
import java.util.Calendar.TUESDAY
import java.util.Calendar.WEDNESDAY

object FakeAlarmData {
    val alarms = listOf(
        Alarm(
            id = 1,
            hour = Hour(6),
            minute = Minute(0),
            weeklyRepeat = WeeklyRepeat(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY),
            label = Label("Wake Up"),
            enabled = true,
            alertType = AlertType.SOUND_ONLY,
            ringtone = Ringtone("file://wake_up_alarm.mp3"),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(
                "I am peaceful and whole.",
                "I do all things in love.",
                "I embrace change seamlessly and rise to the new opportunity it presents.",
            ),
        ),
        Alarm(
            id = 2,
            hour = Hour(20),
            minute = Minute(30),
            weeklyRepeat = WeeklyRepeat(MONDAY, WEDNESDAY, FRIDAY),
            label = Label("Workout"),
            enabled = true,
            alertType = AlertType.VIBRATE_ONLY,
            ringtone = Ringtone("file://workout_time_alarm.mp3"),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(
                "To keep the body in good health is a duty otherwise " +
                    "we shall not be able to keep our mind strong and clear.",
                "My body is strong.",
                "Fitness is my therapy.",
            ),
        ),
        Alarm(
            id = 3,
            hour = Hour(9),
            minute = Minute(0),
            weeklyRepeat = WeeklyRepeat(SUNDAY, SATURDAY),
            label = Label("Passion Hour"),
            enabled = false,
            alertType = AlertType.SOUND_AND_VIBRATE,
            ringtone = Ringtone("file://passion_hour_ringtone.mp3"),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts(
                "I'm ready to dive into my passion",
                "Ready to explore, ready to learn.",
                "I embrace this hour with enthusiasm.",
            ),
        ),
    )

    val selectableRepeats: List<SelectableRepeat>
        get() = (Calendar.SUNDAY..Calendar.SATURDAY).map {
            SelectableRepeat(
                WeekdayFormatterFake().formatFull(it),
                it,
                false,
            )
        }

    val selectableAlertTypes: List<SelectableAlertType>
        get() = AlertType.entries.map {
            SelectableAlertType(
                AlertTypeFormatterFake().format(it),
                it == AlertType.SOUND_AND_VIBRATE,
            )
        }
}
