plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val projectPackage = "org.a_cyb.sayitalarm.system_service.permission_checker"

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

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    // configurations.all {
    //     resolutionStrategy {
    //         force("androidx.test:monitor:1.2.0")
    //     }
    // }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("io.insert-koin:koin-android:3.5.6")

    // Debug
    testImplementation("androidx.compose.ui:ui-test-junit4-android:1.6.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.24")
    testImplementation("org.robolectric:robolectric:4.12")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation(project(":util"))
}
