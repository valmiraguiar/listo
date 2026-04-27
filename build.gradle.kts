import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.hilt.android) apply false
}

subprojects {
    plugins.withId("com.android.application") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
    }

    plugins.withId("org.jetbrains.kotlin.android") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
    }

//    plugins.withId("io.gitlab.arturbosch.detekt") {
//        tasks.matching { it.name == "preBuild" }.configureEach {
//            dependsOn("detekt")
//        }
//    }

    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.matching { it.name == "check" }.configureEach {
            dependsOn("detekt")
        }
    }

    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure<DetektExtension> {
            toolVersion = "1.23.8"
            buildUponDefaultConfig = true
            allRules = false
            autoCorrect = true
            parallel = true
            ignoreFailures = false
            config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        }

        dependencies {
            add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
        }

        tasks.withType<Detekt>().configureEach {
            reports {
                xml.required.set(true)
                html.required.set(true)
                md.required.set(true)
                txt.required.set(false)
                sarif.required.set(false)

                xml.outputLocation.set(file("$rootDir/reports/detekt/${name}.xml"))
                html.outputLocation.set(file("$rootDir/reports/detekt/${name}.html"))
                txt.outputLocation.set(file("$rootDir/reports/detekt/${name}.txt"))
            }

            exclude(
                "**/build/**",
                "**/generated/**"
            )
        }
    }
}
