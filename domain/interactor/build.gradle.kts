plugins {
    alias(libs.plugins.sayitalarm.jvm.library)
}

dependencies {
    implementation(projects.entity)
    implementation(projects.domain.repository)
    implementation(projects.domain.alarmService)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    // Debug
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
}