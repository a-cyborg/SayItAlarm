plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":entity"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}