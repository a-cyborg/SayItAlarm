plugins {
    alias(libs.plugins.sayitalarm.android.application)
}

val projectPackage = "org.a_cyb.sayitalarm"

android {
    namespace = projectPackage

    defaultConfig {
        applicationId = "org.a_cyb.sayitalarm"
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
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

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    configurations.implementation {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(projects.entity)
    implementation(projects.designSystem)
    implementation(projects.domain.interactor)
    implementation(projects.database)
    implementation(projects.data)
    implementation(projects.presentation)
    implementation(projects.presentation.viewmodel)
    implementation(projects.presentation.formatter)
    implementation(projects.alarmService.core)
    implementation(projects.systemService.ringtoneResolver)

    coreLibraryDesugaring(libs.android.tools.desugarJdk)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Debug
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk)
}
