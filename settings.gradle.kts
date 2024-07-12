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
    ":domain:interactor",
    ":domain:repository",
    ":domain:alarm-service",
    ":data",
    ":database",
    ":presentation",
    ":presentation:viewmodel",
    ":presentation:formatter",
    ":alarm-service",
    ":system-service:ringtone-resolver",
    ":system-service:permission-manager",
    ":util",
)

rootProject.name = "SayItAlarm"
