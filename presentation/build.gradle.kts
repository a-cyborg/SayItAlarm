plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Debug
    implementation(kotlin("test"))

    implementation(project(":entity"))
    implementation(project(":util"))
}

