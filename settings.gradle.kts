pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        google()

        mavenLocal()
        //maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()

        mavenLocal()
        //maven("https://jitpack.io")
    }
}

rootProject.name = "Shopme"
include(":app")
include(":data")
include(":domain")
include(":common")
include(":nav")
include(":feature-seller")
include(":feature-customer")
include(":feature-auth")
