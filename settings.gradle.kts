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
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "AttendenceHR"

// Core modules
include(":core:common")
include(":core:database")
include(":core:network")
include(":core:ui")

// Feature modules
include(":features:attendance")
include(":features:employee")
include(":features:dashboard")
include(":features:settings")
include(":features:auth")

// App module
include(":app")
