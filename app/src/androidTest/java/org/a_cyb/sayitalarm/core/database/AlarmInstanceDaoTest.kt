package org.a_cyb.sayitalarm.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.core.database.dao.AlarmInstanceDao
import org.a_cyb.sayitalarm.core.database.model.AlarmInstanceEntity
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AlarmInstanceDaoTest {

    private lateinit var db: SiaDatabase
    private lateinit var instanceDao: AlarmInstanceDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, SiaDatabase::class.java).build()
        instanceDao = db.alarmInstanceDao()
    }

    @After
    @Throws(IOException::class)
    fun cleanup() {
        db.close()
    }

    @Test
    fun instantDao_insert_alarm_and_fetch_alarm() =
        runTest {
            val instanceEntity = testInstanceEntity(id = 3)

            instanceDao.insertAlarm(instanceEntity)

            val savedInstance = instanceDao.getAlarmInstance(3).first()!!

            TestCase.assertEquals(instanceEntity.id, savedInstance.id)
            TestCase.assertEquals(instanceEntity.year, savedInstance.year)
            TestCase.assertEquals(instanceEntity.month, savedInstance.month)
            TestCase.assertEquals(instanceEntity.day, savedInstance.day)
            TestCase.assertEquals(instanceEntity.hour, savedInstance.hour)
            TestCase.assertEquals(instanceEntity.minute, savedInstance.minute)
            TestCase.assertEquals(instanceEntity.label, savedInstance.label)
            TestCase.assertEquals(instanceEntity.vibrate, savedInstance.vibrate)
            TestCase.assertEquals(instanceEntity.ringtone, savedInstance.ringtone)
            TestCase.assertEquals(instanceEntity.associatedAlarmId, savedInstance.associatedAlarmId)
            TestCase.assertEquals(instanceEntity.alarmState, savedInstance.alarmState)
        }
}

private fun testInstanceEntity(
    id: Int = AlarmInstance.INVALID_ID,
    year: Int = 0,
    month: Int = 0,
    day: Int = 0,
    hour: Int = 0,
    minute: Int = 0,
    label: String = "",
    vibrate: Boolean = false,
    ringtone: String = "",
    associatedAlarmId: Int? = null,
    alarmState: Int = 0,
) = AlarmInstanceEntity(
    id = id,
    year = year,
    month = month,
    day = day,
    hour = hour,
    minute = minute,
    label = label,
    vibrate = vibrate,
    ringtone = ringtone,
    associatedAlarmId = associatedAlarmId,
    alarmState = alarmState,
)

 /*
private fun testAlarmEntity(
    id: Int = Alarm.INVALID_ID.toInt(),
    combinedMinute: Int = 0,
    enabled: Boolean = true,
    weeklyRepeat: WeeklyRepeat = WeeklyRepeat.EVERYDAY,
    label: String = "",
    vibrate: Boolean = true,
    ringtone: String = "",
    alarmTerminator: AlarmTerminator = VoiceRecognitionTerminator(listOf("test1", "test2", "test3")),
    alarmOptionalFeature: AlarmOptionalFeature = DreamQuestion(""),
) = AlarmEntity(
    id = id,
    combinedMinutes = combinedMinute,
    enabled = enabled,
    weeklyRepeat = weeklyRepeat,
    label = label,
    vibrate = vibrate,
    ringtone = ringtone,
    alarmTerminator = alarmTerminator,
    alarmOptionalFeature = alarmOptionalFeature
)
  */