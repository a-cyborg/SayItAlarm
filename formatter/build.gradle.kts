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

    // Debug
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.12")
    val mockkVersion = "1.13.11"
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("tech.antibytes.kfixture:core:0.4.0")
    testImplementation(project(":util"))
}
