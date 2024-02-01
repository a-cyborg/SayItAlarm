package org.a_cyb.sayitalarm.core.model

import android.net.Uri

data class UserData(
    /**
     * The number of minutes an alarm may ring before it has timed out and becomes missed.
     */
    val timeOut: Int = 60,
    /**
     * The number of minutes an alarm will remain snoozed before it rings again.
     */
    val snoozeLength: Int = 5,
    val defaultAlarmRingtoneUri: Uri? = Uri.EMPTY,
    val ringtoneCrescendoDuration: Int = 0,
    val volumeButtonBehavior: AlarmVolumeButtonBehavior = AlarmVolumeButtonBehavior.NOTHING,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
)