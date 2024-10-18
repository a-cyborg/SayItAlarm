plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.data"

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.entity)
    implementation(projects.domain.repository)
    implementation(projects.database)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.sqldelight.coroutinesExt)
    implementation(libs.koin.android)

    // Debug
    testImplementation(projects.util.testUtils)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.sqldelight.sqliteDriver)
    testImplementation(libs.mockk)
    testImplementation(libs.antibytes.fixture)
}
