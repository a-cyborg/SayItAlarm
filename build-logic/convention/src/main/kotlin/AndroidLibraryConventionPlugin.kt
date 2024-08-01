/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import com.android.build.api.dsl.LibraryExtension
import org.a_cyb.sayitalarm.build_logic.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                @Suppress("DEPRECATION")
                defaultConfig.targetSdk = 34
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }

            dependencies {
                add("testImplementation", kotlin("test"))
                add("testImplementation", project(":util"))
            }
        }
    }
}
