package org.a_cyb.sayitalarm.core.alarm

import android.app.NotificationManager
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.app.NotificationCompat
import androidx.test.rule.GrantPermissionRule
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AlarmSchedulerTest {
    private lateinit var context: Context
    private lateinit var manager: NotificationManager

    @Before
    fun setup() {
        composeTestRule.setContent {
            context = LocalContext.current
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @JvmField
    @Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS,
        android.Manifest.permission.USE_FULL_SCREEN_INTENT,
        android.Manifest.permission.SCHEDULE_EXACT_ALARM,
    )

    @Test
    fun setAlarm_showsNotification_after3Sec() {
        composeTestRule.apply {
            val currentTime = this.mainClock.currentTime
            val alarmTime = Calendar.getInstance().apply {
                timeInMillis = currentTime
                add(Calendar.SECOND, 3)
            }

            val testAlarmInstance = AlarmInstance(
                year = alarmTime[Calendar.YEAR],
                month = alarmTime[Calendar.MONTH],
                day = alarmTime[Calendar.DAY_OF_MONTH],
                hour = alarmTime[Calendar.HOUR_OF_DAY],
                minute = alarmTime[Calendar.MINUTE],
                alarmState = 0,
                associatedAlarmId = Alarm.INVALID_ID,
            )

            AlarmManagerScheduler.setAlarm(context, testAlarmInstance)

            waitUntil(15_000) { manager.activeNotifications.isNotEmpty() }

            with(manager.activeNotifications.first()) {
                assertEquals(818, id) // ALARM_FIRING_NOTIFICATION_ID
                assertEquals(NotificationCompat.CATEGORY_ALARM, notification.category)
                assertEquals(NotificationCompat.VISIBILITY_PUBLIC, notification.visibility)
                assertEquals(NotificationCompat.PRIORITY_MAX, notification.priority)
                assertEquals("siaAlarmNotification", notification.channelId)
                assertTrue(isOngoing)
                assertTrue(notification.fullScreenIntent.isActivity)
                assertTrue(notification.fullScreenIntent.isImmutable)
                Assert.assertFalse(this.isClearable)
                assertEquals(0L, this.notification.`when`)
            }
        }
    }
}