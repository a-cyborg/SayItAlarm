/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

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
