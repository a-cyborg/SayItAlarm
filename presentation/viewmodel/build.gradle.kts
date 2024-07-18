plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.presentation.viewmodel"

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(projects.entity)
    implementation(projects.domain.interactor)
    implementation(projects.presentation)
    implementation(projects.presentation.formatter)
    implementation(projects.systemService.ringtoneResolver)

    coreLibraryDesugaring(libs.android.tools.desugarJdk)
    implementation(libs.androidx.lifecycle.viewmodelAndroid)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Debug
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.antibytes.fixture)
    testImplementation(libs.mockk)
}
