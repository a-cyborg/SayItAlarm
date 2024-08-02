/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState.Error
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState.Initial
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState.Ringing
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController.AlarmServiceState.RunningSayIt
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmServiceControllerSpec {

    private lateinit var controller: AlarmServiceController

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        controller = AlarmServiceController()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When the service is bound, it sets the state to Ringing`() = runTest {
        // Given
        val alarmService: AlarmServiceContract.AlarmService = mockk()

        controller.alarmState.test {
            skipItems(1)

            // When
            controller.onServiceBind(alarmService)

            // Then
            awaitItem() mustBe Ringing
        }
    }

    @Test
    fun `When the service is disconnected, it sets the state to Initial`() = runTest {
        // Given
        val alarmService: AlarmServiceContract.AlarmService = mockk()

        controller.alarmState.test {
            controller.onServiceBind(alarmService)
            skipItems(2)

            // When
            controller.onServiceDisconnected()

            // Then
            awaitItem() mustBe Initial
        }
    }

    @Test
    fun `When the service is bound and startSayIt is called, it sets the state to RunningSayIt and invokes the service function`() =
        runTest {
            // Given
            val alarmService: AlarmServiceContract.AlarmService = mockk(relaxed = true)

            controller.alarmState.test {
                controller.onServiceBind(alarmService)
                skipItems(2)

                // When
                controller.startSayIt()

                // Then
                awaitItem() mustBe RunningSayIt
            }

            verify(exactly = 1) { alarmService.startSayIt() }
        }

    @Test
    fun `When the service is unbound and startSayIt is called, it sets the state to error`() = runTest {
        controller.alarmState.test {
            // Given
            skipItems(1)

            // When
            controller.startSayIt()

            // Then
            awaitItem() mustBe Error
        }
    }

    @Test
    fun `It is in the Initial state`() {
        val controller = AlarmServiceController()

        controller.alarmState.value mustBe Initial
    }

    @Test
    fun `It fulfills AlarmServiceControllerContract`() {
        AlarmServiceController() fulfils AlarmServiceContract.AlarmServiceController::class
    }
}