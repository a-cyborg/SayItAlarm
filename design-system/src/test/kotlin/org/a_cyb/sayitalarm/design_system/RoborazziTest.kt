/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.a_cyb.sayitalarm.util.test_utils.createAddActivityToRobolectricRule
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
abstract class RoborazziTest(
    captureType: RoborazziRule.CaptureType = RoborazziRule.CaptureType.None,
) {

    @get:Rule(order = 1)
    val addActivityRule = createAddActivityToRobolectricRule()

    @get:Rule(order = 2)
    val subjectUnderTest = createAndroidComposeRule<ComponentActivity>()

    @get:Rule(order = 3)
    val roborazziRule = roborazziOf(subjectUnderTest, captureType)

    @After
    fun capture() {
        subjectUnderTest.onRoot().captureRoboImage()
        subjectUnderTest.activityRule.scenario.recreate()
    }
}
