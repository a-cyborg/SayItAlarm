/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

class EditDistanceCalculatorSpec {

    @Test
    fun `calculateEditDistance IdenticalSentences`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolor sit amet"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 0
    }

    @Test
    fun `calculateEditDistance SingleCharacterChange`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolor sit amef"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 1
    }

    @Test
    fun `calculateEditDistance SingleCharacterRemoval`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolorr sit amet"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 1
    }

    @Test
    fun `calculateEditDistance SingleCharacterInsertion`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsu dolor sit amet"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 1
    }

    @Test
    fun `calculateEditDistance MultipleCharactersReplaces`() {
        // Given
        //             V        V        V    V
        val source = "Lorem ipsum dolor sit amet"
        val target = "Larem ipsun dolor sut amyt"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 4
    }

    @Test
    fun `calculateEditDistance MixedCharacterEdits`() {
        // Given
        //                     V                V                 V V
        val source = "Lorem ipsum dolor sit amet, consectetur adipiscing elit"
        val target = "Lorem ipsom dolor sit amett, consectetur adipsicing elit"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 4
    }

    @Test
    fun `calculateEditDistance empty source`() {
        // Given
        val source = ""
        val target = "Lorem ipsum dolor sit amet."

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 22
    }

    @Test
    fun `calculateEditDistance empty target`() {
        // Given
        val source = "Lorem ipsum dolor sit amet."
        val target = ""

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 22
    }

    @Test
    fun `calculateEditDistance ignores periods`() {
        // Given
        val source = "Lorem ipsum dolor sit amet."
        val target = "Lorem ipsum dolor sit amet"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 0
    }

    @Test
    fun `calculateEditDistance is not case sensitive`() {
        // Given
        val source = "Lorem ipsum dolor Sit Amet"
        val target = "lorem ipsum dolor sit amet"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 0
    }

    @Test
    fun `calculateEditDistance ignores spaces`() {
        // Given
        val source = "Lorem ipsum dolor sit amet"
        val target = "Lorem ipsum dolor sit a met"

        // Then
        EditDistanceCalculator.calculateEditDistance(source, target) mustBe 0
    }

    @Test
    fun `It fulfills EditDistanceCalculator`() {
        EditDistanceCalculator fulfils AlarmServiceContract.EditDistanceCalculator::class
    }
}
