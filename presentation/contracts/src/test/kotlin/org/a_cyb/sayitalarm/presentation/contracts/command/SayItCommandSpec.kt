/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.contracts.command.SayItCommandContract.Finish
import org.a_cyb.sayitalarm.presentation.contracts.command.SayItCommandContract.ProcessScript
import org.junit.Test
import kotlin.test.assertIs

class SayItCommandSpec {

    @Test
    fun `ProcessScriptCommand fulfills Command`() {
        assertIs<Command<ProcessScript>>(ProcessScriptCommand)
    }

    @Test
    fun `When ProcessScriptCommand is executed, it runs processScript`() {
        // Given
        val receiver: ProcessScript = mockk(relaxed = true)

        // When
        ProcessScriptCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.processScript() }
    }

    @Test
    fun `ForceQuit command fulfills Command`() {
        assertIs<Command<ProcessScript>>(FinishCommand)
    }

    @Test
    fun `When ForceQuiCommand is executed, it runs forceQuit`() {
        // Given
        val receiver: Finish = mockk(relaxed = true)

        // When
        FinishCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.finish() }
    }
}
