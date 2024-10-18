/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.time_flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.junit.Test
import java.time.LocalTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimeFlowSpec {
    @Test
    fun `It emits current hour and minutes`() = runTest {
        // Given
        val now = LocalTime.now()

        // When
        val (actualHour, actualMinutes) = TimeFlow.currentTimeFlow
            .take(1)
            .toList()
            .first()

        // Then
        assertEquals(now.hour, actualHour.hour)
        assertTrue(actualMinutes.minute == now.minute || actualMinutes.minute == now.minute + 1)
    }

    @Test
    fun `It emits time with a 5-seconds delay`() = runTest {
        // Given
        val emittedTimes = mutableListOf<Pair<Hour, Minute>>()

        TimeFlow.currentTimeFlow
            .onEach(emittedTimes::add)
            .launchIn(backgroundScope)

        // When
        delay(1)
        // Then
        assertEquals(1, emittedTimes.size)

        // When
        delay(5000)
        // Then
        assertEquals(2, emittedTimes.size)

        // When
        delay(15000)
        // Then
        assertEquals(5, emittedTimes.size)
    }
}
