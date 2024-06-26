/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.command

import kotlin.test.Test
import io.mockk.mockk
import io.mockk.verify
import org.a_cyb.sayitalarm.util.fulfils

class AddCommandSpec {

    @Test
    fun `SaveCommand fulfils Command`() {
        SaveCommand fulfils CommandContract.Command::class
    }

    @Test
    fun `Given SaveCommand executes it runs save`() {
        // Given
        val receiver: AddCommandContract.Save = mockk(relaxed = true)

        // When
        SaveCommand.execute(receiver)

        // Then
        verify(exactly = 1) { receiver.save() }
    }
}
