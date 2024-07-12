plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val projectPackage = "org.a_cyb.sayitalarm.presentation.formatter"

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

    implementation(libs.koin.android)

    // Debug
    testImplementation(kotlin("test"))
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk)
    testImplementation(libs.antibytes.fixture)
    testImplementation(projects.util)
}
