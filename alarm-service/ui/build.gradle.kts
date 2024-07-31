plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service.ui"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    dependencies {
        implementation(projects.designSystem)
        implementation(projects.presentation)
        implementation(projects.presentation.viewmodel)

        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)

        implementation(libs.androidx.activity.compose)
    }
}
