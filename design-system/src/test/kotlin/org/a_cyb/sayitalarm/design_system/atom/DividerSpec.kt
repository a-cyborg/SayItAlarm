/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class DividerSpec : RoborazziTest() {

    @Test
    fun `It renders DividerStandard`() {
        subjectUnderTest.setContent {
            DividerStandard()
        }
    }
}
