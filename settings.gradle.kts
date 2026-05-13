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
        maven {
            url = uri("https://maven.pkg.github.com/boysmtv/android-mtv-based-uicomponent")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: System.getenv("GITHUB_PACKAGES_USER")
                password = System.getenv("GITHUB_TOKEN")
                    ?: System.getenv("GITHUB_PACKAGES_TOKEN")
            }
        }
        maven {
            url = uri("https://maven.pkg.github.com/boysmtv/android-mtv-based-core")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: System.getenv("GITHUB_PACKAGES_USER")
                password = System.getenv("GITHUB_TOKEN")
                    ?: System.getenv("GITHUB_PACKAGES_TOKEN")
            }
        }
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
include(":feature-firebase")
include(":core")
