plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

val compilerPlugin by configurations.creating


dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("tools.aqua:z3-turnkey:4.12.2.1")

    // From https://github.com/sschr15/advent-of-code/tree/2024/z3
    implementation(files("z3/kotlin-z3-wrapper.jar"))

    implementation("com.sschr15.aoc:runtime-components:0.1.0")
    compilerPlugin("com.sschr15.aoc:compiler-plugin:0.1.0")
}

kotlin {
    compilerOptions {
        for (plugin in compilerPlugin) {
            freeCompilerArgs.add("-Xplugin=${plugin.absolutePath}")
        }
    }
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.2.1"
    }
}