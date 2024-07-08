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
    ":formatter",
    ":alarm-service",
    ":ringtone-resolver",
    ":util",
)

rootProject.name = "SayItAlarm"
