/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class ActionRowSpec : RoborazziTest() {

    @Test
    fun `It renders ActionRowCancelAndConfirm`() {
        subjectUnderTest.setContent {
            ActionRowCancelAndConfirm(
                onCancel = {},
                onConfirm = {},
            )
        }
    }

    @Test
    fun `It renders ActionRowCollapse`() {
        subjectUnderTest.setContent {
            ActionRowCollapse {}
        }
    }
}
