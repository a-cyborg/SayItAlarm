plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("io.github.takahirom.roborazzi")

    kotlin("kapt")
}

android {
    namespace = "org.a_cyb.sayitalarm"
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

        // TODO: Currently using a hard-coded path for the schema directory. In the future,
        //  I'll investigate more dynamic methods to determine the path of the database package or
        //  module for improved flexibility and maintainability.
        val dbPath = "/src/main/java/org/a_cyb/sayitalarm/core/database"
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/$dbPath/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        kotlinCompilerExtensionVersion = "1.4.3"
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

    val roomVersion = "2.6.0"
    val composeBom = platform("androidx.compose:compose-bom:2023.03.00")
    val navigationVersion = "2.7.6"

    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // DI
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // Data
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.datastore:datastore:1.0.0")

    // Util
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Test
    implementation(kotlin("test"))
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.test:rules:1.5.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    implementation("androidx.compose.ui:ui-test-junit4-android:1.6.7")
    androidTestImplementation("androidx.navigation:navigation-testing:$navigationVersion")
    androidTestImplementation(composeBom)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")

    val roborazziVersion = "1.15.0"
    testImplementation("io.github.takahirom.roborazzi:roborazzi:$roborazziVersion")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:$roborazziVersion")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:$roborazziVersion")

    testImplementation("org.robolectric:robolectric:4.12")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // Module
    implementation(project(":entity"))
}

kapt {
    correctErrorTypes = true
}