import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

tasks {
    create<ShadowJar>("LogProxyDemoJar") {
        archiveBaseName.set("LogProxyDemo")
        archiveVersion.set("")
        archiveClassifier.set("")
        manifest {
            attributes(
                    "Main-Class" to "ru.otus.aop.proxy.LogProxyDemo"
            )
        }
        from(sourceSets.main.get().output)
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
    build {
        dependsOn( "LogProxyDemoJar")
    }
}