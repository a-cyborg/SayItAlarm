/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("androidx-compose-compiler").get().requiredVersion
        }

        @Suppress("UnstableApiUsage")
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }

        dependencies {
            "implementation"(libs.findLibrary("androidx.activity.compose").get())

            "debugImplementation"(libs.findLibrary("androidx.compose.ui.testManifest").get())
            "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
            "testImplementation"(libs.findLibrary("androidx.compose.ui.test").get())
            "testImplementation"(kotlin("test"))
            "testImplementation"(project(":util"))
        }
    }
}
