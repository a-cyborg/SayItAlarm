plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.entity)

    implementation(libs.kotlinx.coroutines.core)

    // Debug
    testImplementation(libs.mockk)
}
