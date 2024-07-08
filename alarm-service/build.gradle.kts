plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":domain:alarm-service"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.insert-koin:koin-core:3.5.6")

    // Debug
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
}