package org.a_cyb.sayitalarm.core.datastore

import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import org.a_cyb.sayitalarm.DarkThemeConfigProto
import org.a_cyb.sayitalarm.UserPreferences
import org.a_cyb.sayitalarm.VolumeButtonBehaviorProto
import org.a_cyb.sayitalarm.copy
import org.a_cyb.sayitalarm.core.model.AlarmVolumeButtonBehavior
import org.a_cyb.sayitalarm.core.model.DarkThemeConfig
import org.a_cyb.sayitalarm.core.model.UserData
import org.a_cyb.sayitalarm.util.TAG
import java.io.IOException
import javax.inject.Inject

class SiaUserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userData = userPreferences.data
        .map {
            if (it == UserPreferences.getDefaultInstance()) {
                // Here I want to check if the UserData is factory new which does not hold the right
                // default value for UserData object. Return System defined UserData() object and update
                // proto file.
                // I think ths check should be move to somewhere in the future.
                UserData()
                    .apply { setDefaultUserDataForTheFirstTime(this) }
            } else {
                UserData(
                    timeOut = it.timeOut,
                    snoozeLength = it.snoozeLength,
                    defaultAlarmRingtoneUri = Uri.parse(it.defaultAlarmRingtoneUri),
                    ringtoneCrescendoDuration = it.ringtoneCrescendoDuration,
                    volumeButtonBehavior = when(it.volumeButtonBehavior) {
                        VolumeButtonBehaviorProto.DISMISS -> AlarmVolumeButtonBehavior.DISMISS
                        VolumeButtonBehaviorProto.SNOOZE -> AlarmVolumeButtonBehavior.SNOOZE
                        null, VolumeButtonBehaviorProto.NOTHING,
                        VolumeButtonBehaviorProto.UNRECOGNIZED -> AlarmVolumeButtonBehavior.NOTHING
                    },
                    darkThemeConfig = when(it.darkTheme) {
                        DarkThemeConfigProto.DARK -> DarkThemeConfig.DARK
                        DarkThemeConfigProto.LIGHT -> DarkThemeConfig.LIGHT
                        null, DarkThemeConfigProto.UNRECOGNIZED,
                        DarkThemeConfigProto.FOLLOW_SYSTEM -> DarkThemeConfig.FOLLOW_SYSTEM
                    }
                )
            }
        }

    /**
     * This function sets the application-defined default value for the very first time of
     * fetch user data (This logic should be updated in the future)
     */
    private suspend fun setDefaultUserDataForTheFirstTime(defaultUserData: UserData) {
        try {
            userPreferences.updateData {
                it.copy {
                    timeOut = defaultUserData.timeOut
                    snoozeLength = defaultUserData.snoozeLength
                    ringtoneCrescendoDuration = defaultUserData.ringtoneCrescendoDuration
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setAlarmTimeOut: Failed to update  user preferences", e)
        }
    }

    suspend fun setTimeOut(min: Int) {
        try {
            userPreferences.updateData {
                it.copy {
                    timeOut = min
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setAlarmTimeOut: Failed to update  user preferences", e)
        }
    }

    suspend fun setSnoozeLength(min: Int) {
        try {
            userPreferences.updateData {
                it.copy {
                     snoozeLength= min
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setAlarmTimeOut: Failed to update user preferences", e)
        }
    }

    suspend fun setDefaultAlarmRingtoneUri(uri: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    defaultAlarmRingtoneUri = uri
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setDefaultAlarmRingtoneUri: Failed to update uri on user preferences", e)
        }
    }

    suspend fun setCrescendoDuration(min: Int) {
        try {
            userPreferences.updateData {
                it.copy {
                    ringtoneCrescendoDuration = min
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setAlarmTimeOut: Failed to update user preferences", e)
        }
    }

    suspend fun setVolumeButtonBehavior(behavior: AlarmVolumeButtonBehavior) {
        try {
            userPreferences.updateData {
                it.copy {
                    volumeButtonBehavior = when(behavior) {
                        AlarmVolumeButtonBehavior.SNOOZE -> VolumeButtonBehaviorProto.SNOOZE
                        AlarmVolumeButtonBehavior.DISMISS -> VolumeButtonBehaviorProto.DISMISS
                        AlarmVolumeButtonBehavior.NOTHING -> VolumeButtonBehaviorProto.NOTHING
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setAlarmTimeOut: Failed to update user preferences", e)
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    darkTheme = when(darkThemeConfig) {
                        DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK
                        DarkThemeConfig.LIGHT -> DarkThemeConfigProto.LIGHT
                        DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.FOLLOW_SYSTEM
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "setAlarmTimeOut: Failed to update user preferences", e)
        }
    }
}

//    val userData = userPreferences.data
//        .map {
//            UserData(
//                timeOutInMin = it.timeOut,
//                snoozeLength = it.snoozeLength,
//                // [***] DEV Mode
//                defaultAlarmRingtoneUri = URI.create(it.defaultAlarmRingtoneUri),
//                ringtoneCrescendoDuration = it.ringtoneCrescendoDuration,
//                volumeButtonBehavior = when (it.volumeButtonBehavior) {
//                    VolumeButtonBehaviorProto.DISMISS -> AlarmVolumeButtonBehavior.DISMISS
//                    VolumeButtonBehaviorProto.SNOOZE -> AlarmVolumeButtonBehavior.SNOOZE
//                    null, VolumeButtonBehaviorProto.NOTHING,
//                    VolumeButtonBehaviorProto.UNRECOGNIZED -> AlarmVolumeButtonBehavior.NOTHING
//                },
//                darkThemeConfig = when (it.darkTheme) {
//                    DarkThemeConfigProto.DARK -> DarkThemeConfig.DARK
//                    DarkThemeConfigProto.LIGHT -> DarkThemeConfig.LIGHT
//                    null, DarkThemeConfigProto.UNRECOGNIZED,
//                    DarkThemeConfigProto.FOLLOW_SYSTEM -> DarkThemeConfig.FOLLOW_SYSTEM
//                },
//            )
//        }
