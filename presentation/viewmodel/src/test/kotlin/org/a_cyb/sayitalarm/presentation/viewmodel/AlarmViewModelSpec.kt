/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.TimeFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlow
import org.a_cyb.sayitalarm.presentation.viewmodel.time_flow.TimeFlowContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModelSpec {

    private val timeFormatter: TimeFormatterContract = TimeFormatterFake()
    private val timeFlow: TimeFlowContract = mockk(relaxed = true)
    private lateinit var viewModel: AlarmViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = AlarmViewModel(timeFormatter, timeFlow)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It start in the Initial state`() {
        viewModel.state.value mustBe AlarmContract.AlarmState.Initial
    }

    @Test
    fun `When in the Initial state, it collects the currentTime and formats it into a string`() = runTest {
        // Given
        val currentTimes = listOf(
            Pair(Hour(11), Minute(9)),
            Pair(Hour(11), Minute(10)),
            Pair(Hour(11), Minute(11)),
        )
        val timeFlow = object : TimeFlowContract {
            override val currentTimeFlow: Flow<Pair<Hour, Minute>> =
                flow {
                    currentTimes.forEach {
                        emit(it)
                    }
                }
        }
        val viewModel = AlarmViewModel(timeFormatter, timeFlow)

        val expected = currentTimes.map { (hour, minute) ->
            timeFormatter.format(hour, minute)
        }

        // When
        viewModel.currentTime.test {
            skipItems(1) // Initial
            // Then
            awaitItem() mustBe expected[0]
            awaitItem() mustBe expected[1]
            awaitItem() mustBe expected[2]
        }
    }

    @Test
    fun `When it is not in the Initial state, it does not collects the currentTime`() = runTest {
        // Given
        val viewModel = AlarmViewModel(timeFormatter, TimeFlow)

        viewModel.currentTime.test {
            skipItems(2) // Initial & One currentTime item

            // When
            viewModel.startSayIt()  // It goes into the VoiceInputProcessing state.

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `It fulfills AlarmViewModel`() {
        viewModel fulfils AlarmContract.AlarmViewModel::class
    }
}