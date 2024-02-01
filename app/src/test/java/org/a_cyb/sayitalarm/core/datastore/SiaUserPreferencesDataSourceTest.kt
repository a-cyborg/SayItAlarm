package org.a_cyb.sayitalarm.core.datastore

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.DarkThemeConfigProto
import org.a_cyb.sayitalarm.UserPreferences
import org.a_cyb.sayitalarm.VolumeButtonBehaviorProto
import org.a_cyb.sayitalarm.core.model.UserData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class SiaUserPreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var dataStore: DataStore<UserPreferences>
    private lateinit var dataSource: SiaUserPreferencesDataSource

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setUp() {
        dataStore =
            DataStoreFactory.create(
                serializer = UserPreferencesSerializer(),
                scope = testScope,
                corruptionHandler = null,
                produceFile = { tempFolder.newFile("test_user_preferences.pb")  }
            )

        dataSource = SiaUserPreferencesDataSource(dataStore)
    }

    @Test
    fun userPrefsDataSource_default_user_data_is_correct() = testScope.runTest {
        assertEquals(
            UserData(),
            dataSource.userData.first()
        )
    }

    @Test
    fun userPrefsDataSource_shouldWriteDefault_afterFirstFetch() = testScope.runTest {
        // Before the first fetch return a proto-defined default.
        val factoryDefault = dataStore.data.first()

        assertEquals(0, factoryDefault.timeOut)
        assertEquals(0, factoryDefault.snoozeLength)
        assertEquals("", factoryDefault.defaultAlarmRingtoneUri)
        assertEquals(0, factoryDefault.ringtoneCrescendoDuration)
        assertEquals(VolumeButtonBehaviorProto.NOTHING, factoryDefault.volumeButtonBehavior)
        assertEquals(DarkThemeConfigProto.FOLLOW_SYSTEM, factoryDefault.darkTheme)

        // Given: Fetch userData, dataSource should write an application-defined default value
        dataSource.userData.first()

        val savedUserPrefs = dataStore.data.first()

        val defaultUserData = UserData()

        assertEquals(defaultUserData.timeOut, savedUserPrefs.timeOut)
        assertEquals(defaultUserData.snoozeLength, savedUserPrefs.snoozeLength)
        assertEquals(defaultUserData.defaultAlarmRingtoneUri, Uri.EMPTY)
        assertEquals(defaultUserData.ringtoneCrescendoDuration, savedUserPrefs.ringtoneCrescendoDuration)
        assertEquals(VolumeButtonBehaviorProto.NOTHING, savedUserPrefs.volumeButtonBehavior)
        assertEquals(DarkThemeConfigProto.FOLLOW_SYSTEM, savedUserPrefs.darkTheme)
    }
}
