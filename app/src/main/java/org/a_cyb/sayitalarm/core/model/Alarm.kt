package org.a_cyb.sayitalarm.core.model

import org.a_cyb.sayitalarm.core.alarm.AlarmOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.AlarmTerminator

/**
 * External data layer representation of an Alarm.
 * All alarm data will eventually be accessed via this model.
 */
data class Alarm (
    val id: Int = INVALID_ID,
    val combinedMinutes: CombinedMinutes,
    val weeklyRepeat: WeeklyRepeat,
    val label: String,
    val enabled: Boolean,
    val vibrate: Boolean,
    val ringtone: String,
    val alarmTerminator: AlarmTerminator,
    val alarmOptionalFeature: AlarmOptionalFeature,
) {
    companion object {
        /**
         * Alarms have an invalid ID if they haven't been saved in the database yet.
         */
        const val INVALID_ID = 0
    }
}