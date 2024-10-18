/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
plugins {
    alias(libs.plugins.sayitalarm.jvm.library)
}


dependencies {
    implementation(projects.entity)

    implementation(libs.kotlinx.coroutines.core)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
}
