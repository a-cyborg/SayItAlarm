/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.model

import kotlin.test.Test
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.util.mustBe
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

class SettingsEntitySpec {

    private val fixture = kotlinFixture()

    @Test
    fun `When toSettings is called it maps settingsEntity to Settings`() {
        // Given
        val timeOut: Long = fixture.fixture(range = 30..300)
        val snooze: Long = fixture.fixture(range = 5..30)
        val theme: Long = Theme.DARK.ordinal.toLong()

        val settingsEntity = SettingsEntity(
            timeOut = timeOut,
            snooze = snooze,
            theme = theme,
        )

        // When
        val actual = settingsEntity.toSettings()

        // Then
        actual mustBe Settings(
            timeOut = TimeOut(timeOut.toInt()),
            snooze = Snooze(snooze.toInt()),
            theme = Theme.DARK,
        )
    }

    @Test
    fun `When toSettingsEntity is called it maps settings to SettingsEntity`() {
        val timeOut: Int = fixture.fixture(range = 30..300)
        val snooze: Int = fixture.fixture(range = 5..30)
        val theme: Theme = Theme.LIGHT

        val settings = Settings(
            timeOut = TimeOut(timeOut),
            snooze = Snooze(snooze),
            theme = theme,
        )

        // When
        val actual = settings.toSettingsEntity()

        // Then
        actual mustBe SettingsEntity(
            timeOut = timeOut.toLong(),
            snooze = snooze.toLong(),
            theme = 0
        )
    }
}
