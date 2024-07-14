plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.alarm_service"
}

dependencies {
    implementation(projects.domain.alarmService)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}