package org.a_cyb.sayitalarm.core.alarm

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.core.app.NotificationCompat
import androidx.test.rule.GrantPermissionRule
import org.a_cyb.sayitalarm.core.model.AlarmInstance
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AlarmNotificationTest {

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
            val testInstance = AlarmInstance(alarmState = 0)
            lateinit var manager: NotificationManager

            setContent {
                val context = LocalContext.current

                AlarmNotification.showAlarmNotification(context, testInstance)
                manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }

            waitUntil { manager.activeNotifications.isNotEmpty() }

            // Test the notification.
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
            val testInstance = AlarmInstance(alarmState = 0)
            lateinit var manager: NotificationManager

            setContent {
                with(LocalContext.current) {
                    AlarmNotification.showAlarmNotification(this, testInstance)
                    manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                }
            }

            waitUntil { manager.activeNotifications.isNotEmpty() }

            manager.activeNotifications.first().notification.fullScreenIntent.send()

            onNodeWithText("Say It").assertExists()
            onNodeWithText("Exit").assertExists()
        }
    }

    @Test
    fun whenCancelAlarmNotificationCalled_notificationCanceled() {
        composeTestRule.apply {
            lateinit var context: Context
            lateinit var manager: NotificationManager

            setContent {
                with(LocalContext.current) {
                    AlarmNotification.showAlarmNotification(this, AlarmInstance(alarmState = 0))
                    manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                    context = this
                }
            }

            // Check if notification is exist.
            waitUntil { manager.activeNotifications.isNotEmpty() }
            assertEquals(818, manager.activeNotifications.first().id)

            AlarmNotification.cancelAlarmFiringNotification(context)

            assertTrue(manager.activeNotifications.isEmpty())
        }
    }
}