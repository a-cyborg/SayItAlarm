plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.github.takahirom.roborazzi")
}

val projectPackage = "org.a_cyb.sayitalarm"

android {
    namespace = projectPackage
    compileSdk = 34

    defaultConfig {
        applicationId = "org.a_cyb.sayitalarm"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            matchingFallbacks.add("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kotlin.sourceSets.all {
        languageSettings.enableLanguageFeature("DataObjects")
    }

    configurations.implementation {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")

    // Test
    implementation(kotlin("test"))
    implementation("androidx.compose.ui:ui-test-junit4-android:1.6.7")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.24")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.robolectric:robolectric:4.12")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")

    val roborazziVersion = "1.15.0"
    testImplementation("io.github.takahirom.roborazzi:roborazzi:$roborazziVersion")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:$roborazziVersion")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:$roborazziVersion")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.7")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.7")

    implementation(project(":entity"))
    implementation(project(":presentation"))
    implementation(project(":presentation:viewmodel"))
    implementation(project(":util"))
}
