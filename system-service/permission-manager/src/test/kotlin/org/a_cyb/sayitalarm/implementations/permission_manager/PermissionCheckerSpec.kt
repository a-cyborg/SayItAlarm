package org.a_cyb.sayitalarm.implementations.permission_manager

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.os.PowerManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class PermissionCheckerSpec {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When getMissingEssentialPermission is called it returns missing permissions`() {
        // When
        val permissions = PermissionChecker(context).getMissingEssentialPermission()

        // Then
        permissions mustBe listOf(
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.RECORD_AUDIO,
        )
    }

    @Test
    fun `When getMissingEssentialPermission is called after granting one it returns remaining missing permissions`() {
        // Given
        Shadows.shadowOf(RuntimeEnvironment.getApplication())
            .run { grantPermissions(Manifest.permission.RECORD_AUDIO) }

        val permissions = PermissionChecker(context).getMissingEssentialPermission()

        // Then
        permissions mustBe listOf(
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    }

    @Test
    fun `When getMissingEssentialPermission is called and all the permissions have been granted it returns empty list`() {
        // Given
        val context: Context = mockk()
        val alarmManager: AlarmManager = mockk(relaxed = true)

        every { context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) } returns 0
        every { context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) } returns 0
        every { context.getSystemService(AlarmManager::class.java) } returns alarmManager
        every { alarmManager.canScheduleExactAlarms() } returns true

        // When
        val permissions = PermissionChecker(context).getMissingEssentialPermission()

        // Then
        permissions mustBe emptyList()
    }

    @Test
    fun `When getMissingPermission is called it returns missing permissions`() {
        // When
        val permissions = PermissionChecker(context).getMissingPermission()

        // Then
        permissions mustBe listOf(
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.READ_MEDIA_AUDIO,
        )
    }

    @Test
    fun `When getMissingPermission is called after granting one permission it returns remaining missing permissions`() {
        // Given
        Shadows.shadowOf(RuntimeEnvironment.getApplication())
            .run { grantPermissions(Manifest.permission.READ_MEDIA_AUDIO) }

        val permissions = PermissionChecker(context).getMissingPermission()

        // Then
        permissions mustBe listOf(
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        )
    }

    @Test
    fun `When getMissingPermission is called and all the permissions have been granted it returns empty list`() {
        // Given
        val context: Context = mockk(relaxed = true)
        val notificationManager: NotificationManager = mockk(relaxed = true)
        val powerManager: PowerManager = mockk(relaxed = true)

        every { context.checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) } returns 0
        every { context.getSystemService(NotificationManager::class.java) } returns notificationManager
        every { context.getSystemService(PowerManager::class.java) } returns powerManager

        every { notificationManager.isNotificationPolicyAccessGranted } returns true
        every { powerManager.isIgnoringBatteryOptimizations(context.packageName) } returns true

        // When
        val permissions = PermissionChecker(context).getMissingPermission()

        // Then
        permissions mustBe emptyList()
    }
}
