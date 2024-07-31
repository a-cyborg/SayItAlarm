plugins {
    // alias(libs.plugins.sayitalarm.android.library)
    alias(libs.plugins.sayitalarm.android.library.comopose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "org.a_cyb.sayitalarm.design_system"
    // buildFeatures {
    //     compose = true
    // }
    //
    // composeOptions {
    //     kotlinCompilerExtensionVersion = "1.5.14"
    // }
    //
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            // isIncludeAndroidResources = true
        }
    }

    roborazzi {
        outputDir.set(project.layout.projectDirectory.dir("src/test/snapshots/roborazzi/images"))
    }

    dependencies {
        implementation(projects.entity)
        implementation(projects.presentation)

        // implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.compose.material3)
        implementation(libs.accompanist.permissions)

        // testImplementation(libs.androidx.compose.ui.test)
        // debugImplementation(libs.androidx.compose.ui.testManifest)
        // debugImplementation(libs.androidx.compose.ui.tooling)
        testImplementation(libs.junit.jupiter)
        testImplementation(libs.robolectric)
        testImplementation(libs.bundles.roborazzi)
    }
}
