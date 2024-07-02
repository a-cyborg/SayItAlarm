plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":entity"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Debug
    implementation(kotlin("test"))
    val mockkVersion = "1.13.11"
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation(project(":util"))
}

