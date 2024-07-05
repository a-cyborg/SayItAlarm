plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val projectPackage = "org.a_cyb.sayitalarm.formatter"

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
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("io.insert-koin:koin-android:3.5.6")

    // Debug
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("io.mockk:mockk:1.13.11")
}
