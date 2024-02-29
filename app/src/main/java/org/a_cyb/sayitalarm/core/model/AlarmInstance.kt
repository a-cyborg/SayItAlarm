package org.a_cyb.sayitalarm.core.model

import android.net.Uri
import java.util.Calendar

data class AlarmInstance(
    val id: Int = INVALID_ID,
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    val label: String? = null,
    val vibrate: Boolean = false,  // Not implemented.
    val ringtone: Uri? = null,
    val associatedAlarmId: Int,
    val alarmState: Int
) {
    var alarmTime: Calendar
        get() = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        set(calendar) {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
        }

    companion object {
        const val INVALID_ID = 0
    }
}