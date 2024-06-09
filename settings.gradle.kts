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
    ":util",
)

rootProject.name = "SayItAlarm"
include(":alarm-service")
