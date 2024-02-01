package org.a_cyb.sayitalarm.core.data.repository

import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.datastore.core.DataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.core.datastore.SiaUserPreferencesDataSource
import org.a_cyb.sayitalarm.core.datastore.UserPreferencesSerializer
import org.a_cyb.sayitalarm.core.model.AlarmVolumeButtonBehavior
import org.a_cyb.sayitalarm.core.model.DarkThemeConfig
import org.a_cyb.sayitalarm.core.model.UserData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

class ImplUserDataRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var userDataRepo: UserDataRepository
    private lateinit var prefsDataSource: SiaUserPreferencesDataSource

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        // TODO: Set up test datastore is duplicated make test module
        val dataStore =
            DataStoreFactory.create(
                serializer = UserPreferencesSerializer(),
                scope = testScope,
                corruptionHandler = null,
                produceFile = { tempFolder.newFile("test_user_preferences.pb")  }
            )

        prefsDataSource = SiaUserPreferencesDataSource(dataStore)
        userDataRepo = ImplUserDataRepository(prefsDataSource)
    }

    @Test
    fun userDataRepository_defaultUserData_isCorrect() = testScope.runTest {
        assertEquals(
            UserData(), // Application-defined user data default
            userDataRepo.userData.first()
        )
    }

    @Test
    fun userDataRepo_setTimeOut_delegatesToPrefsDataSource() = runTest {
        userDataRepo.setTimeOut(11)

        assertEquals(
            11,
            userDataRepo.userData.first().timeOut
        )

        userDataRepo.setTimeOut(40)

        assertEquals(
            40,
            userDataRepo.userData.first().timeOut
        )

        assertEquals(
            prefsDataSource.userData.first().timeOut,
            userDataRepo.userData.first().timeOut
        )
    }

    @Test
    fun userDataRepo_setSnoozeLength_delegatesToPrefsDataSource() = runTest {
        userDataRepo.setSnoozeLength(30)

        assertEquals(
            30,
            userDataRepo.userData.first().snoozeLength
        )

        userDataRepo.setSnoozeLength(20)

        assertEquals(
            20,
            userDataRepo.userData.first().snoozeLength
        )

        assertEquals(
            prefsDataSource.userData.first().snoozeLength,
            userDataRepo.userData.first().snoozeLength
        )
    }

    @Test
    fun userDataRepo_setDefaultRingtoneUri_delegatesToPrefsDataSource() = runTest {
        val testFileOne = tempFolder.newFile("ringtone-test-first-file.mp3")

        userDataRepo.setDefaultAlarmRingtoneUri(testFileOne.toUri())

        assertEquals(
            testFileOne,
            userDataRepo.userData.first().defaultAlarmRingtoneUri!!.toFile()
        )

        val testFileTwo = tempFolder.newFile("ringtone-test-second-file.mp3")

        userDataRepo.setDefaultAlarmRingtoneUri(testFileTwo.toUri())

        assertEquals(
            testFileTwo,
            userDataRepo.userData.first().defaultAlarmRingtoneUri!!.toFile()
        )

        assertEquals(
            prefsDataSource.userData.first().defaultAlarmRingtoneUri,
            userDataRepo.userData.first().defaultAlarmRingtoneUri
        )
    }

    @Test
    fun userDataRepo_setRingtoneCrescendoDuration_delegatesToPrefsDataSource() = runTest {
        userDataRepo.setRingtoneCrescendoDuration(7)

        assertEquals(
            7,
            userDataRepo.userData.first().ringtoneCrescendoDuration
        )

        userDataRepo.setRingtoneCrescendoDuration(9)

        assertEquals(
            9,
            userDataRepo.userData.first().ringtoneCrescendoDuration
        )

        assertEquals(
            prefsDataSource.userData.first().ringtoneCrescendoDuration,
            userDataRepo.userData.first().ringtoneCrescendoDuration
        )
    }

    @Test
    fun userDataRepo_setVolumeButtonBehavior_delegatesToPrefsDataSource() = runTest {
        userDataRepo.setVolumeButtonBehavior(AlarmVolumeButtonBehavior.SNOOZE)

        assertEquals(
            AlarmVolumeButtonBehavior.SNOOZE,
            userDataRepo.userData.first().volumeButtonBehavior
        )

        userDataRepo.setVolumeButtonBehavior(AlarmVolumeButtonBehavior.DISMISS)

        assertEquals(
            AlarmVolumeButtonBehavior.DISMISS,
            userDataRepo.userData.first().volumeButtonBehavior
        )

        assertEquals(
            prefsDataSource.userData.first().volumeButtonBehavior,
            userDataRepo.userData.first().volumeButtonBehavior
        )
    }

    @Test
    fun userDataRepo_setDarkThemeConfig_delegatesToPrefsDataSource() = runTest {
        userDataRepo.setDarkThemeConfig(DarkThemeConfig.LIGHT)

        assertEquals(
            DarkThemeConfig.LIGHT,
            userDataRepo.userData.first().darkThemeConfig
        )

        userDataRepo.setDarkThemeConfig(DarkThemeConfig.DARK)

        assertEquals(
            DarkThemeConfig.DARK,
            userDataRepo.userData.first().darkThemeConfig
        )

        assertEquals(
            prefsDataSource.userData.first().darkThemeConfig,
            userDataRepo.userData.first().darkThemeConfig
        )
    }
}