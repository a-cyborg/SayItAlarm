pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SayItAlarm"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":app",
    ":entity",
    ":domain:interactor",
    ":domain:repository",
    ":domain:alarm-service",
    ":data",
    ":database",
    ":presentation",
    ":presentation:viewmodel",
    ":presentation:formatter",
    ":alarm-service",
    ":alarm-service:ui",
    ":system-service:ringtone-resolver",
    ":system-service:permission-manager",
    ":util",
)
