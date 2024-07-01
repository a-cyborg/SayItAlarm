pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(
    ":app",
    ":entity",
    ":domain",
    ":data",
    ":database",
    ":presentation",
    ":presentation:viewmodel",
    ":presentation:interactor",
    ":formatter",
    ":alarm-service",
    ":ringtone-manager",
    ":util",
)

rootProject.name = "SayItAlarm"
