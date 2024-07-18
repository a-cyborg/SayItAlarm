plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service"

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
}


dependencies {
    implementation(projects.entity)
    implementation(projects.domain.alarmService)
    implementation(projects.domain.repository)

    coreLibraryDesugaring(libs.android.tools.desugarJdk)
    implementation(libs.androidx.work.workRuntime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.android)

    // Debug
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.work.workTesting)
}