plugins {
    alias(libs.plugins.sayitalarm.android.library.comopose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "org.a_cyb.sayitalarm.design_system"

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    roborazzi {
        outputDir.set(project.layout.projectDirectory.dir("src/test/snapshots/roborazzi/images"))
    }

    dependencies {
        implementation(projects.entity)
        implementation(projects.presentation.contracts)

        implementation(libs.androidx.compose.material3)
        implementation(libs.accompanist.permissions)
        implementation(libs.abooutLibraries.ui)

        testImplementation(libs.junit.jupiter)
        testImplementation(libs.robolectric)
        testImplementation(libs.bundles.roborazzi)
        testImplementation(projects.util.testUtils)
        testImplementation(libs.androidx.compose.ui.testManifest)
    }
}
