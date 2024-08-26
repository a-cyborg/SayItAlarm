plugins {
    alias(libs.plugins.sayitalarm.android.library)
}

android {
    namespace = "org.a_cyb.sayitalarm.presentation.sound_effect_player"
}

dependencies {
    implementation(libs.koin.android)
}
