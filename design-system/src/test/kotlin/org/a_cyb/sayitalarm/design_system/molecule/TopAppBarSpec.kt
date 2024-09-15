/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.design_system.atom.IconButtonAdd
import org.a_cyb.sayitalarm.design_system.atom.IconButtonNavigateBack
import org.a_cyb.sayitalarm.design_system.atom.IconButtonSettings
import org.a_cyb.sayitalarm.design_system.atom.TextButtonEdit
import org.junit.Test

class TopAppBarSpec : RoborazziTest() {

    private fun stringRes(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders TopAppBarSmall`() {
        subjectUnderTest.setContent {
            TopAppBarSmall(
                title = "SayIt",
                firstIcon = { TextButtonEdit {} },
                secondIcon = { IconButtonAdd {} },
                thirdIcon = { IconButtonSettings {} },
            )
        }
    }

    @Test
    fun `When using TopAppBarSmall with the firstIcon parameter, it displays a single icon`() {
        subjectUnderTest.setContent {
            TopAppBarSmall(
                title = "SayIt",
                firstIcon = { IconButtonNavigateBack {} },
            )
        }

        subjectUnderTest
            .onNodeWithContentDescription(stringRes(R.string.action_navigate_back))
            .assertExists()
    }

    @Test
    fun `When using TopAppBarSmall with both firstIcon and secondIcon, it displays both icons`() {
        subjectUnderTest.setContent {
            TopAppBarSmall(
                title = "SayIt",
                firstIcon = { IconButtonNavigateBack {} },
                secondIcon = { IconButtonSettings {} },
            )
        }

        with(subjectUnderTest) {
            onNodeWithContentDescription(stringRes(R.string.action_navigate_back)).assertExists()
            onNodeWithContentDescription(stringRes(R.string.action_open_settings)).assertExists()
        }
    }

    @Test
    fun `It renders TopAppBarLarge`() {
        with(subjectUnderTest) {
            setContent {
                TopAppBarLarge(
                    title = stringRes(R.string.say_it),
                    actions = {
                        TextButtonEdit {}
                        IconButtonAdd {}
                        IconButtonSettings {}
                    },
                )
            }

            // Verify all actions are displayed
            onNodeWithText(stringRes(R.string.edit)).assertExists()
            onNodeWithContentDescription(stringRes(R.string.action_add_alarm)).assertExists()
            onNodeWithContentDescription(stringRes(R.string.action_open_settings)).assertExists()
        }
    }

    @Test
    fun `It renders TopAppBarLarge with navigationIcon`() {
        with(subjectUnderTest) {
            setContent {
                TopAppBarLarge(
                    title = stringRes(R.string.say_it),
                    navigationIcon = { IconButtonNavigateBack {} },
                )
            }

            // Verify navigationIcon and Title are displayed
            onNodeWithText(stringRes(R.string.say_it)).assertExists()
            onNodeWithContentDescription(stringRes(R.string.action_navigate_back)).assertExists()
        }
    }
}
