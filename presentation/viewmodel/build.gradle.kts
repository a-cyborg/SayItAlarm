plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val projectPackage = "org.a_cyb.sayitalarm.presentation.viewmodel"

android {
    namespace = projectPackage
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.8.0")

    // Debug
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("tech.antibytes.kfixture:core:0.4.0")
    val mockkVersion = "1.13.11"
    testImplementation("io.mockk:mockk:$mockkVersion")

    implementation(project(":entity"))
    implementation(project(":presentation"))
    implementation(project(":presentation:interactor"))
    implementation(project(":formatter"))
    implementation(project(":alarm-service"))
    implementation(project(":ringtone-manager"))
    implementation(project(":util"))
}
