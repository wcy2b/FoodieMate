pluginManagement {
    repositories {
        maven("https://mirrors.cloud.tencent.com/repository/maven/google")
        maven("https://mirrors.cloud.tencent.com/repository/maven/maven-central")
        maven("https://mirrors.cloud.tencent.com/repository/maven/gradle-plugins")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://mirrors.cloud.tencent.com/repository/maven/google")
        maven("https://mirrors.cloud.tencent.com/repository/maven/maven-central")
        google()
        mavenCentral()
    }
}

rootProject.name = "FoodieMate"
include(":app")
 