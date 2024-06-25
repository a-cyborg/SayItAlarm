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
    ":presentation",
    ":presentation:viewmodel",
    ":presentation:interactor",
    ":domain",
    ":domain:interactor",
    ":domain:repository",
    ":formatter",
    ":alarm-service",
    ":ringtone-manager",
    ":util",
)

rootProject.name = "SayItAlarm"
