plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val copyJsResources = tasks.create("copyJsResourcesWorkaround", Copy::class.java) {
    dependsOn(":webApp:jsDevelopmentExecutableCompileSync")
    from(project(":shared").file("src/commonMain/resources"))
    into("build/processedResources/js/main")
}

afterEvaluate {
    project.tasks.getByName("jsProcessResources").finalizedBy(copyJsResources)
}

kotlin {
    js(IR) {
        moduleName = "CircularFillableLoader"
        browser()
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(npm("path-browserify", "^1.0.1"))
                implementation(npm("os-browserify", "^0.3.0"))
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
    }
}

compose.experimental {
    web.application {}
}