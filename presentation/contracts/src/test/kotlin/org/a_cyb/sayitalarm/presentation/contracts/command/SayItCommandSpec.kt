/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Test

class SayItCommandSpec {

    @Test
    fun `ProcessScriptCommand fulfills Command`() {
        ProcessScriptCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `When ProcessScriptCommand is executed, it runs processScript`() {
        // Given
        val receiver: SayItCommandContract.ProcessScript = mockk(relaxed = true)

        // When
        ProcessScriptCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.processScript() }
    }

    @Test
    fun `ForceQuit command fulfills Command`() {
        FinishCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `When ForceQuiCommand is executed, it runs forceQuit`() {
        // Given
        val receiver: SayItCommandContract.Finish = mockk(relaxed = true)

        // When
        FinishCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.finish() }
    }
}
