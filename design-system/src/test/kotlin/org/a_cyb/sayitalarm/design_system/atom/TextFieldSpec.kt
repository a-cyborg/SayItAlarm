/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class TextFieldSpec : RoborazziTest() {

    @Test
    fun `It renders TextFieldStandardAlignEnd`() {
        subjectUnderTest.setContent {
            TextFieldLabel(
                text = "",
                onValueChange = { _ -> }
            )
        }
    }

    @Test
    fun `It renders TextFieldSayItScript`() {
        subjectUnderTest.setContent {
            TextFieldSayItScript(
                text = "SayIt",
                onValueChange = { _ -> }
            )
        }
    }
}
