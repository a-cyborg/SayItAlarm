plugins {
    id("org.jetbrains.kotlin.jvm")
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    api(project(":entity"))
}
