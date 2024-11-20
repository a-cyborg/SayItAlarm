/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service.scheduler"

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            all {
                it.jvmArgs(
                    "--add-opens",
                    "java.base/java.time=ALL-UNNAMED",
                    "-Djdk.attach.allowAttachSelf=true"
                )
            }

            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.entity)
    implementation(projects.domain.repository)
    implementation(projects.domain.alarmService)
    implementation(projects.alarmService.core)
    implementation(libs.androidx.work.workRuntime)
    implementation(libs.koin.core)
    coreLibraryDesugaring(libs.android.tools.desugarJdk)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.work.workTesting)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk)
    testImplementation(libs.koin.core)
    testImplementation(libs.koin.android)
    testImplementation(libs.antibytes.fixture)
}