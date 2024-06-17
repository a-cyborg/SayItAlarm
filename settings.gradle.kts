pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
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
    ":formatter",
    ":alarm-service",
    ":ringtone-manager",
    ":util",
)

rootProject.name = "SayItAlarm"
