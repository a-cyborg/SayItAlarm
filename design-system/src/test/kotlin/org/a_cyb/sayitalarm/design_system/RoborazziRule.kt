/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.RoborazziRule

fun roborazziOf(
    scenarioRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    captureType: RoborazziRule.CaptureType = RoborazziRule.CaptureType.None,
): RoborazziRule {
    return RoborazziRule(
        composeRule = scenarioRule,
        captureRoot = scenarioRule.onRoot(),
        options = RoborazziRule.Options(
            captureType = captureType,
            outputDirectoryPath = "src/test/snapshots/roborazzi/images",
        ),
    )
}
