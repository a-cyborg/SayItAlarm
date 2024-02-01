package org.a_cyb.sayitalarm.core.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import org.a_cyb.sayitalarm.core.datastore.SiaUserPreferencesDataSource
import org.a_cyb.sayitalarm.core.model.AlarmVolumeButtonBehavior
import org.a_cyb.sayitalarm.core.model.DarkThemeConfig
import org.a_cyb.sayitalarm.core.model.UserData
import javax.inject.Inject

class ImplUserDataRepository @Inject constructor(
    private val siaUserPreferencesDataSource: SiaUserPreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData> = siaUserPreferencesDataSource.userData

    override suspend fun setTimeOut(min: Int) =
        siaUserPreferencesDataSource.setTimeOut(min)

    override suspend fun setSnoozeLength(min: Int) =
        siaUserPreferencesDataSource.setSnoozeLength(min)

    override suspend fun setDefaultAlarmRingtoneUri(uri: Uri) =
        siaUserPreferencesDataSource.setDefaultAlarmRingtoneUri(uri.toString())

    override suspend fun setRingtoneCrescendoDuration(min: Int) =
        siaUserPreferencesDataSource.setCrescendoDuration(min)

    override suspend fun setVolumeButtonBehavior(volumeButtonBehavior: AlarmVolumeButtonBehavior) =
        siaUserPreferencesDataSource.setVolumeButtonBehavior(volumeButtonBehavior)

    override suspend fun setDarkThemeConfig(darkTheme: DarkThemeConfig) =
        siaUserPreferencesDataSource.setDarkThemeConfig(darkTheme)
}