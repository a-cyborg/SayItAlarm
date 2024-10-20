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
    ":util:formatter",
    ":util:link-opener",
    ":util:ringtone-resolver",
    ":util:sound-effect-player",
    ":util:time-flow",
    ":util:test-utils",
)
