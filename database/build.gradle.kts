plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")

    id("app.cash.sqldelight") version "2.0.2"
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
    implementation("app.cash.sqldelight:android-driver:2.0.2")

    // Debug
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("app.cash.sqldelight:sqlite-driver:2.0.0")
    testImplementation("tech.antibytes.kfixture:core:0.4.0")
    testImplementation(project(":util"))
}
