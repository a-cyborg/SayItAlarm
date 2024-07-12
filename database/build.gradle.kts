plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.sqldelight)
}

val projectPackage = "org.a_cyb.sayitalarm.database"
val dbName = "SayItDB"

android {
    namespace = projectPackage
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
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
    testImplementation(kotlin("test"))
    testImplementation(libs.sqldelight.sqliteDriver)
    testImplementation(libs.antibytes.fixture)
    testImplementation(libs.mockk)
    testImplementation(projects.util)
}
