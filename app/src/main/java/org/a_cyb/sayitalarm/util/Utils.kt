package org.a_cyb.sayitalarm.util

import android.os.Build
import org.a_cyb.sayitalarm.core.alarm.AlarmOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.AlarmTerminator
import org.a_cyb.sayitalarm.core.alarm.DreamQuestion
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import java.util.Calendar
import java.util.Random

// Extension function to provide log TAG value.
val Any.TAG: String
    get() = javaClass.simpleName

/* Build version checkers */
/**
 * @return true if the device is [Build.VERSION_CODES.O] or later.
 */
val isBuildVersionOOrLater: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

/**
 * @return true if the device is [Build.VERSION_CODES.O_MR1] or later.
 */
val isBuildVersionOMR1OrLater: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1


// Dev mode
// For dev mode create randomly generated Alarm.
fun getRandomAlarm(
    combinedMinutes: CombinedMinutes = CombinedMinutes((0..1439).random()),
    enabled: Boolean = Random().nextBoolean(),
    weeklyRepeat: WeeklyRepeat = WeeklyRepeat((1..7).random(), (1..7).random()),
    label: String = "",
    vibrate: Boolean = Random().nextBoolean(),
    ringtone: String = "",
    alarmTerminator: AlarmTerminator = VoiceRecognitionTerminator(listOf("Hi", "Awesome")),
    alarmOptionalFeature: AlarmOptionalFeature = DreamQuestion(""),
) =
    Alarm (
        combinedMinutes = combinedMinutes,
        enabled = enabled,
        weeklyRepeat = weeklyRepeat,
        label = label,
        vibrate = vibrate,
        ringtone = ringtone,
        alarmTerminator = alarmTerminator,
        alarmOptionalFeature = alarmOptionalFeature,
    )

fun getCurrentTime(): String {
    val calendarInstance = Calendar.getInstance()
    val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
    val minute = calendarInstance.get(Calendar.MINUTE)
    val second = calendarInstance.get(Calendar.SECOND)
    return "$hour : $minute : $second"
}