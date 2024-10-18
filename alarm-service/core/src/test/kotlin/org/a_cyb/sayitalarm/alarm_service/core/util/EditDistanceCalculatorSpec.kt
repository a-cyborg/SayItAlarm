/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class EditDistanceCalculatorSpec {

    @Test
    fun `calculateEditDistance IdenticalSentences`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolor sit amet"

        // Then
        assertEquals(0, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance SingleCharacterChange`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolor sit amef"

        // Then
        assertEquals(1, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance SingleCharacterRemoval`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolorr sit amet"

        // Then
        assertEquals(1, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance SingleCharacterInsertion`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsu dolor sit amet"

        // Then
        assertEquals(1, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance MultipleCharactersReplaces`() {
        // Given
        //             V        V        V    V
        val source = "Lorem ipsum dolor sit amet"
        val target = "Larem ipsun dolor sut amyt"

        // Then
        assertEquals(4, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance MixedCharacterEdits`() {
        // Given
        //                     V                V                 V V
        val source = "Lorem ipsum dolor sit amet, consectetur adipiscing elit"
        val target = "Lorem ipsom dolor sit amett, consectetur adipsicing elit"

        // Then
        assertEquals(4, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance empty source`() {
        // Given
        val source = ""
        val target = "Lorem ipsum dolor sit amet."

        // Then
        assertEquals(22, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance empty target`() {
        // Given
        val source = "Lorem ipsum dolor sit amet."
        val target = ""

        // Then
        assertEquals(22, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance ignores periods`() {
        // Given
        val source = "Lorem ipsum dolor sit amet."
        val target = "Lorem ipsum dolor sit amet"

        // Then
        assertEquals(0, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance is not case sensitive`() {
        // Given
        val source = "Lorem ipsum dolor Sit Amet"
        val target = "lorem ipsum dolor sit amet"

        // Then
        assertEquals(0, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `calculateEditDistance ignores spaces`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolor sit a met"

        // Then
        assertEquals(0, EditDistanceCalculator.calculateEditDistance(source, target))
    }

    @Test
    fun `It fulfills EditDistanceCalculator`() {
        assertIs<AlarmServiceContract.EditDistanceCalculator>(EditDistanceCalculator)
    }
}
