package org.a_cyb.sayitalarm.core.alarm

import android.app.NotificationManager
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.core.app.NotificationCompat
import androidx.test.rule.GrantPermissionRule
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AlarmNotificationTest {
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
    )

    @Test
    fun whenShowAlarmNotificationCalled_notificationNotified() {
        composeTestRule.apply {
            AlarmNotification.showAlarmNotification(context, getDummyAlarmInstance())

            waitUntil { manager.activeNotifications.isNotEmpty() }

            with(manager.activeNotifications.first()) {
                assertEquals(818, id) // ALARM_FIRING_NOTIFICATION_ID
                assertEquals(NotificationCompat.CATEGORY_ALARM, notification.category)
                assertEquals(NotificationCompat.VISIBILITY_PUBLIC, notification.visibility)
                assertEquals(NotificationCompat.PRIORITY_MAX, notification.priority)
                assertEquals("siaAlarmNotification", notification.channelId)
                assertTrue(isOngoing)
                assertTrue(notification.fullScreenIntent.isActivity)
                assertTrue(notification.fullScreenIntent.isImmutable)
                assertFalse(this.isClearable)
                assertEquals(0L, this.notification.`when`)
            }
        }
    }

    @Test
    fun whenShowAlarmNotificationCalled_fullScreenNotificationDisplayed() {
        composeTestRule.apply {
            AlarmNotification.showAlarmNotification(context, getDummyAlarmInstance())

            waitUntil { manager.activeNotifications.isNotEmpty() }

            manager.activeNotifications.first().notification.fullScreenIntent.send()

            onNodeWithText("Say It").assertExists()
            onNodeWithText("Exit").assertExists()
        }
    }

    @Test
    fun whenCancelAlarmNotificationCalled_notificationCanceled() {
        composeTestRule.apply {
            AlarmNotification.showAlarmNotification(context, getDummyAlarmInstance())

            waitUntil { manager.activeNotifications.isNotEmpty() }

            // To Confirm that notification is exists.
            assertEquals(818, manager.activeNotifications.first().id)

            AlarmNotification.cancelAlarmFiringNotification(context)

            assertTrue(manager.activeNotifications.isEmpty())
        }
    }

    private fun getDummyAlarmInstance() =
        AlarmInstance(associatedAlarmId = Alarm.INVALID_ID, alarmState = 0)
}