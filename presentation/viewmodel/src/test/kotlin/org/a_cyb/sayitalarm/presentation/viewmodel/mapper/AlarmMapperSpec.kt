/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.mapper

import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.AlertTypeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.FakeAlarmData
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.RingtoneResolverFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.util.formatter.enum.EnumFormatterContract
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.util.ringtone_resolver.RingtoneResolverContract
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import java.util.Calendar
import kotlin.test.Test

class AlarmMapperSpec {
    private val timeFormatter: TimeFormatterContract = TimeFormatterFake()
    private val weeklyRepeatFormatter: WeekdayFormatterContract = WeekdayFormatterFake()
    private val alertTypeFormatter: EnumFormatterContract.AlertTypeFormatter = AlertTypeFormatterFake()
    private val ringtoneManager: RingtoneResolverContract = RingtoneResolverFake()
    private val mapper: AlarmMapperContract =
        AlarmMapper(
            timeFormatter,
            weeklyRepeatFormatter,
            alertTypeFormatter,
            ringtoneManager,
        )

    @Test
    fun `Given AlarmMapper mapToAlarm is called it maps AlarmUI to Alarm`() {
        // Given
        val alarm = testAlarm.copy(id = 0, enabled = true)
        val alarmUI = testAlarmUI

        // When
        val mapped = mapper.mapToAlarm(alarmUI)

        // Then
        mapped mustBe alarm
    }

    @Test
    fun `Given AlarmMapper mapToAlarmUI is called it maps Alarm to AlarmUI`() {
        // Given
        val alarm = testAlarm
        val alarmUI = testAlarmUI

        // When
        val mapped = mapper.mapToAlarmUI(alarm)

        // Then
        mapped mustBe alarmUI
    }

    private val testAlarm = FakeAlarmData.alarms[2]

    private val testAlarmUI = AlarmUI(
        timeUI = TimeUI(
            hour = 9,
            minute = 0,
            formattedTime = "9:00 AM",
        ),
        weeklyRepeatUI = WeeklyRepeatUI(
            formatted = "every weekend",
            selectableRepeats = FakeAlarmData.selectableRepeats.map {
                it.copy(selected = it.code in listOf(Calendar.SATURDAY, Calendar.SUNDAY))
            },
        ),
        label = testAlarm.label.label,
        alertTypeUI = AlertTypeUI(
            FakeAlarmData.selectableAlertTypes.map {
                it.copy(selected = it.name == alertTypeFormatter.format(AlertType.SOUND_AND_VIBRATE))
            },
        ),
        ringtoneUI = RingtoneUI(
            title = "passion_hour_ringtone",
            uri = testAlarm.ringtone.ringtone,
        ),
        sayItScripts = testAlarm.sayItScripts.scripts,
    )
}
