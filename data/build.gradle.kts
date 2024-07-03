plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val projectPackage = "org.a_cyb.sayitalarm.data"

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
    implementation(project(":entity"))
    implementation(project(":domain"))
    implementation(project(":database"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")

    // Debug
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.sqldelight:sqlite-driver:2.0.0")
    testImplementation("tech.antibytes.kfixture:core:0.4.0")
    testImplementation(project(":util"))
}
