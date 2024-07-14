plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.system_service.ringtone_resolver"
}

dependencies {
    implementation(libs.koin.android)

    // Debug
    testImplementation(libs.mockk)
}
