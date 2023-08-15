rootProject.name = "otus_java_2023_06"
include("hw01-gradle")
include("L04-generics")
include("hw06-annotations")
include("L08-gc")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings


    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}
include("hw06-annotations:src:main:untitled")
findProject(":hw06-annotations:src:main:untitled")?.name = "untitled"
include("hw06-annotations")
