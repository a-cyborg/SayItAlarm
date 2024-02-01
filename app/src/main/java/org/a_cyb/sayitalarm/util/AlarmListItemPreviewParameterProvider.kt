package org.a_cyb.sayitalarm.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.a_cyb.sayitalarm.feature.list.AlarmListItem
import org.a_cyb.sayitalarm.util.PreviewParameterData.alarmListItems
import java.util.Calendar

class AlarmListItemPreviewParameterProvider : PreviewParameterProvider<List<AlarmListItem>> {

    override val values: Sequence<List<AlarmListItem>> = sequenceOf(alarmListItems)
}

object PreviewParameterData {
    val alarmListItems = listOf(
        AlarmListItem(
            id = 1,
            time = CombinedMinutes(13, 13),
            label = "Lunch",
            weeklyRepeat = WeeklyRepeat.EVERYDAY,
            enabled = true
        ),
        AlarmListItem(
            id = 2,
            time = CombinedMinutes(9, 30),
            label = "Morning",
            weeklyRepeat = WeeklyRepeat(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY),
            enabled = true
        ),
        AlarmListItem(
            id = 3,
            time = CombinedMinutes(11, 55),
            label = "",
            weeklyRepeat = WeeklyRepeat.NEVER,
            enabled = false
        ),
        AlarmListItem(
            id = 4,
            time = CombinedMinutes(3, 33),
            label = "",
            weeklyRepeat = WeeklyRepeat(Calendar.MONDAY, Calendar.TUESDAY,Calendar.WEDNESDAY,
                Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY),
            enabled = true
        ),
    )
}
