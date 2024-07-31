plugins {
    alias(libs.plugins.sayitalarm.android.library.comopose)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service.ui"

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    dependencies {
        implementation(projects.designSystem)
        implementation(projects.presentation)
        implementation(projects.presentation.viewmodel)

        coreLibraryDesugaring(libs.android.tools.desugarJdk)
        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)
    }
}
