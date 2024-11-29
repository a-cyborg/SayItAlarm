plugins {
    alias(libs.plugins.sayitalarm.android.library.comopose)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service.core"

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    lint {
        disable += "ExactAlarm"
        abortOnError = false
        checkReleaseBuilds = false
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        animationsDisabled = true

        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.entity)
    implementation(projects.domain.alarmService)
    implementation(projects.domain.repository)
    implementation(projects.presentation.contracts)
    implementation(projects.presentation.viewmodel)
    implementation(projects.designSystem)
    implementation(projects.util.audioVibePlayer)

    coreLibraryDesugaring(libs.android.tools.desugarJdk)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    testImplementation(projects.util.testUtils)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.antibytes.fixture)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(projects.util.audioVibePlayer)
}
