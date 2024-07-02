plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":entity"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Debug
    implementation(kotlin("test"))
    testImplementation(project(":util"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("io.mockk:mockk:1.13.11")
}
