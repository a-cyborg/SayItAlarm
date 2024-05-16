/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.atom.IconButtonAdd
import org.a_cyb.sayitalarm.atom.IconButtonEditText
import org.a_cyb.sayitalarm.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.atom.IconButtonSettings
import org.a_cyb.sayitalarm.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class TopAppBarSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders TopAppBarGlobal`() {
        subjectUnderTest.setContent {
            TopAppBarGlobal(
                title = "SayIt",
                firstIcon = { IconButtonEditText {} },
                secondIcon = { IconButtonAdd {} },
                thirdIcon = { IconButtonSettings {} },
            )
        }
    }

    @Test
    fun `Given TopAppBarGlobal with firstIcon it displays one icon`() {
        subjectUnderTest.setContent {
            TopAppBarGlobal(
                title = "SayIt",
                firstIcon = { IconButtonNavigateBack {} },
            )
        }

        subjectUnderTest
            .onNodeWithContentDescription(getString(R.string.action_navigate_back))
            .assertExists()
    }

    @Test
    fun `Given TopAppBarGlobal with firstIcon and secondIcon it displays both icons`() {
        subjectUnderTest.setContent {
            TopAppBarGlobal(
                title = "SayIt",
                firstIcon = { IconButtonNavigateBack {} },
                secondIcon = { IconButtonSettings {} },
            )
        }

        with(subjectUnderTest) {
            onNodeWithContentDescription(getString(R.string.action_navigate_back)).assertExists()
            onNodeWithContentDescription(getString(R.string.action_open_settings)).assertExists()
        }
    }
}
