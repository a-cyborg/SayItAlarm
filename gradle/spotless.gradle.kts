/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

val ktlintVersion = "1.0.1"

initscript {
    val spotlessVersion = "6.25.0"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
    }
}

rootProject {
    subprojects {
        apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
        extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            kotlin {
                target("**/*.kt")
                targetExclude("**/build/**/*.kt")
                ktlint(ktlintVersion).editorConfigOverride(
                    mapOf(
                        "android" to "true",
                        "ktlint_standard_package-name" to "disabled",
                        "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                    ),
                )
            }
            format("kts") {
                target("**/*.kts")
                targetExclude("**/build/**/*.kts")
            }
            format("xml") {
                target("**/*.xml")
                targetExclude("**/build/**/*.xml")
            }
        }
    }
}