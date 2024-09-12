/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.mapper

import java.time.DayOfWeek
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlertTypeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableAlertType
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.system_service.ringtone_resolver.RingtoneResolverContract

class AlarmMapper(
    private val timeFormatter: org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract,
    private val weeklyRepeatFormatter: org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatterContract,
    private val alertTypeFormatter: org.a_cyb.sayitalarm.util.formatter.enum.EnumFormatterContract.AlertTypeFormatter,
    private val ringtoneResolver: RingtoneResolverContract,
) : AlarmMapperContract {

    /*
    * AlarmUI -> Alarm
    * */
    override fun mapToAlarm(alarmUI: AlarmUI): Alarm =
        with(alarmUI) {
            Alarm(
                hour = Hour(timeUI.hour),
                minute = Minute(timeUI.minute),
                weeklyRepeat = weeklyRepeatUI.toWeeklyRepeat(),
                label = Label(label),
                enabled = true,
                alertType = alertTypeUI.toAlertType(),
                ringtone = Ringtone(ringtoneUI.uri),
                alarmType = AlarmType.SAY_IT,
                sayItScripts = SayItScripts(sayItScripts)
            )
        }

    private fun WeeklyRepeatUI.toWeeklyRepeat(): WeeklyRepeat {
        val weekdays = selectableRepeats
            .filter { it.selected }
            .map { it.code }

        return WeeklyRepeat(weekdays.toSortedSet())
    }

    private fun AlertTypeUI.toAlertType(): AlertType {
        val name: String = selectableAlertType
            .first { it.selected }
            .name

        return alertTypeMap[name] ?: DEFAULT_ALERT_TYPE
    }

    private val alertTypeMap: Map<String, AlertType>
        get() = AlertType.entries
            .associateBy { alertTypeFormatter.format(it) }

    /*
    * Alarm -> AlarmUI
    * */
    override fun mapToAlarmUI(alarm: Alarm): AlarmUI =
        with(alarm) {
            AlarmUI(
                Pair(hour, minute).toTimeUI(),
                weeklyRepeat.toWeeklyRepeatUI(),
                label.label,
                alertType.toAlertTypeUI(),
                ringtone.toRingtoneUI(),
                sayItScripts.scripts
            )
        }

    private fun Pair<Hour, Minute>.toTimeUI(): TimeUI {
        return TimeUI(
            hour = first.hour,
            minute = second.minute,
            formattedTime = timeFormatter.format(first, second)
        )
    }

    private fun WeeklyRepeat.toWeeklyRepeatUI(): WeeklyRepeatUI {
        val formatted = weeklyRepeatFormatter.formatAbbr(weekdays)
        val selectableRepeats = weekdays.toSelectableRepeats()

        return WeeklyRepeatUI(formatted, selectableRepeats)
    }

    private fun Set<Int>.toSelectableRepeats(): List<SelectableRepeat> {
        return WEEKLY_REPEAT_RANGE.map { code ->
            SelectableRepeat(
                name = weeklyRepeatFormatter.formatFull(code),
                code = code,
                selected = contains(code)
            )
        }
    }

    private fun AlertType.toAlertTypeUI(): AlertTypeUI {
        val selectableAlertTypes = AlertType.entries.map { alertType ->
            SelectableAlertType(
                name = alertTypeFormatter.format(alertType),
                selected = this == alertType
            )
        }

        return AlertTypeUI(selectableAlertTypes)
    }

    private fun Ringtone.toRingtoneUI(): RingtoneUI {
        val uri = ringtone.ifEmpty { ringtoneResolver.getDefaultRingtone().getOrNull() }

        return if (uri != null) {
            val title = ringtoneResolver.getRingtoneTitle(uri).getOrNull()

            RingtoneUI(title ?: "No title", uri)
        } else {
            RingtoneUI("", "")
        }
    }

    companion object {
        private val DEFAULT_ALERT_TYPE = AlertType.SOUND_AND_VIBRATE
        private val WEEKLY_REPEAT_RANGE = (DayOfWeek.MONDAY.value..DayOfWeek.SUNDAY.value)
    }
}
