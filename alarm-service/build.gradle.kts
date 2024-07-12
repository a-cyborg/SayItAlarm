plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val projectPackage = "org.a_cyb.sayitalarm.alarm_service"

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
    implementation(projects.domain.alarmService)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    // Debug
    testImplementation(kotlin("test"))
}