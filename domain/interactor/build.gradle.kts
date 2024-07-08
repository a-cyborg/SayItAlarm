plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":entity"))
    implementation(project(":domain:repository"))
    implementation(project(":domain:alarm-service"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.insert-koin:koin-core:3.5.6")

    // Debug
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation(project(":util"))
}