package org.a_cyb.sayitalarm.util

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import java.text.DateFormatSymbols
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun getFormattedClockTime(context: Context, calendar: Calendar): String {
    val timeFormat = DateFormat.getTimeFormat(context).apply {
        timeZone =   when {
            isBuildVersionOOrLater -> TimeZone.getTimeZone(ZoneId.systemDefault())
            else -> TimeZone.getDefault()
        }
    }

    return timeFormat.format(calendar.timeInMillis)
}

@Composable
fun getFormattedClockTime(combinedMin: CombinedMinutes): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, combinedMin.hour)
        set(Calendar.MINUTE, combinedMin.minute)
    }

    return getFormattedClockTime(LocalContext.current, calendar)
}

@Composable
fun getLocalizedShortWeekDayFormatted(weeklyRepeat: WeeklyRepeat): String =
    when(weeklyRepeat) {
        WeeklyRepeat.EVERYDAY -> stringResource(id = R.string.every_day)
        else -> getLocalizedShortWeekdays(weeklyRepeat)
    }

fun getLocalizedShortWeekdays(weeklyRepeat: WeeklyRepeat): String {
    val shortWeekdays = DateFormatSymbols(Locale.getDefault()).shortWeekdays

    return weeklyRepeat.weekdays
        .joinToString(separator = ", ", transform = shortWeekdays::get)
}

fun getLocalizedFullWeekdaysMap(): Map<Int, String>  {
    val fullWeekdays = DateFormatSymbols(Locale.getDefault()).weekdays

    return WeeklyRepeat.EVERYDAY.weekdays
        .associateWith(fullWeekdays::get)
}