plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val projectPackage = "org.a_cyb.sayitalarm.data"

android {
    namespace = projectPackage
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    kotlinOptions {
        jvmTarget = "17"
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
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.sqldelight.sqliteDriver)
    testImplementation(libs.mockk)
    testImplementation(libs.antibytes.fixture)
    testImplementation(projects.util)
}
