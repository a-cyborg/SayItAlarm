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
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import java.time.LocalTime

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
        actualHour.hour mustBe now.hour
        (actualMinutes.minute == now.minute || actualMinutes.minute == now.minute + 1) mustBe true
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
        emittedTimes.size mustBe 1

        // When
        delay(5000)
        // Then
        emittedTimes.size mustBe 2

        // When
        delay(15000)
        // Then
        emittedTimes.size mustBe 5
    }
}
