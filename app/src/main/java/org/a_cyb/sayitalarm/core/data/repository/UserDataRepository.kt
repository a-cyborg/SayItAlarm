package org.a_cyb.sayitalarm.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.core.model.AlarmVolumeButtonBehavior
import org.a_cyb.sayitalarm.core.model.DarkThemeConfig
import org.a_cyb.sayitalarm.core.model.UserData

interface UserDataRepository {

    /**
     * Get userdata as a flow stream
     */
    val userData: Flow<UserData>

    /**
     * Sets the number of minutes an alarm may ring before it has timed out and becomes missed
     */
    suspend fun setTimeOut(min: Int)

    /**
     * Sets the number of minutes an alarm will remain snoozed before it rings again
     */
    suspend fun setSnoozeLength(min: Int)

    /**
     * Sets the default alarm uri
     */
    suspend fun setDefaultAlarmRingtoneUri(uri: Uri)

    /**
     * Sets the number of minutes an alarm ring crescendo duration(Optional)
     */
   suspend fun setRingtoneCrescendoDuration(min: Int)

    /**
     * Sets the alarm volume behavior
     */
   suspend fun setVolumeButtonBehavior(volumeButtonBehavior: AlarmVolumeButtonBehavior)

    /**
     * Sets the dark theme config
     */
   suspend fun setDarkThemeConfig(darkTheme: DarkThemeConfig)
}