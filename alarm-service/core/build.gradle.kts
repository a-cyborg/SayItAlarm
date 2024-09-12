plugins {
    alias(libs.plugins.sayitalarm.android.library.comopose)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service.core"

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
            all { it.jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED") }
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

    coreLibraryDesugaring(libs.android.tools.desugarJdk)
    implementation(libs.androidx.work.workRuntime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(kotlin("test"))
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.work.workTesting)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.antibytes.fixture)
}
