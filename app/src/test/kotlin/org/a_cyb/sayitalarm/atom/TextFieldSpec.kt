/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class TextFieldSpec : RoborazziTest() {

    @Test
    fun `It renders TextFieldStandardAlignEnd`() {
        subjectUnderTest.setContent {
            TextFieldLabel(
                value = "",
                hint = "SayIt",
                onDone = { _ -> }
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
