plugins {
    alias(libs.plugins.sayitalarm.android.library)
    alias(libs.plugins.sqldelight)
}

val projectPackage = "org.a_cyb.sayitalarm.database"
val dbName = "SayItDB"

android {
    namespace = projectPackage
}

sqldelight {
    databases {
        create(dbName) {
            packageName.set(projectPackage)
            srcDirs.setFrom("src/main/schema")
        }
    }
}

dependencies {
    implementation(libs.sqldelight.androidDriver)
    implementation(libs.koin.android)

    // Debug
    testImplementation(projects.util.testUtils)
    testImplementation(libs.sqldelight.sqliteDriver)
    testImplementation(libs.antibytes.fixture)
    testImplementation(libs.mockk)
}
