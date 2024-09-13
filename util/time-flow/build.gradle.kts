plugins {
    alias(libs.plugins.sayitalarm.jvm.library)
}

dependencies {
    implementation(projects.entity)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    testImplementation(libs.kotlinx.coroutines.test)
}
