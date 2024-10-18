/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.util.test_utils"
}

dependencies {
    implementation(kotlin("test"))
    implementation(libs.androidx.test.ext)
    implementation(libs.robolectric)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
