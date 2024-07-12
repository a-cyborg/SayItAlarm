plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.entity)

    implementation(libs.kotlinx.coroutines.core)

    // Debug
    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(projects.util)
}
