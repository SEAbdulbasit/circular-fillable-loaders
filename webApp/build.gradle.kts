import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

kotlin {
    wasm {
        moduleName = "jetsnackwasmapp"
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
//                    open = mapOf(
//                        "app" to mapOf(
//                            "name" to "google chrome canary",
//                            "arguments" to listOf("--js-flags=--experimental-wasm-gc ")
//                        )
//                    ),
                    static = (devServer?.static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.rootDir.path + "/common/")
                        add(project.rootDir.path + "/nonAndroidMain/")
                        add(project.rootDir.path + "/web/")
                    },
                )
            }
        }
        binaries.executable()
//        applyBinaryen()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(project(":shared"))
                implementation(npm("path-browserify", "^1.0.1"))
                implementation(npm("os-browserify", "^0.3.0"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}

// Use a proper version of webpack, TODO remove after updating to Kotlin 1.9.
rootProject.the<NodeJsRootExtension>().versions.webpack.version = "5.76.2"