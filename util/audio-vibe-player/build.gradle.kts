plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.util.audio_vibe_player"
}

dependencies {
    implementation(projects.entity)
    implementation(libs.koin.android)

    // Debug
    testImplementation(libs.mockk)
}
