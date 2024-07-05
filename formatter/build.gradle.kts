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
    implementation("io.insert-koin:koin-android:3.5.6")

    // Debug
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.12")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("tech.antibytes.kfixture:core:0.4.0")
    testImplementation(project(":util"))
}
