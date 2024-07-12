plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.entity)
    implementation(projects.domain.repository)
    implementation(projects.domain.alarmService)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    // Debug
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(projects.util)
}