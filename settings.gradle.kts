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
    ":presentation:contracts",
    ":presentation:viewmodel",
    ":design-system",
    ":alarm-service:core",
    ":system-service:ringtone-resolver",
    ":system-service:permission-manager",
    ":util",
    ":util:link-opener",
    ":util:sound-effect-player",
    ":util:formatter"
)
