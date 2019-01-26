@file:Suppress("UNUSED_VARIABLE", "UnstableApiUsage")

import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("nebula.release") version "9.2.0"
  id("nebula.kotlin") version "1.3.20" apply false
  id("org.jmailen.kotlinter") version "1.20.1" apply false
  id("info.solidsoft.pitest") version "1.3.0" apply false
  id("release-tweet")
}

buildscript {
  configurations.maybeCreate("pitest")
  dependencies {
    "pitest"("org.pitest:pitest-junit5-plugin:0.7")
  }
}

allprojects {
  group = "io.strikt"
}

subprojects {
  apply(plugin = "nebula.release")

  repositories {
    jcenter()
  }

  afterEvaluate {
    plugins.withId("kotlin") {
      configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_8
      }

      tasks.withType<KotlinCompile> {
        kotlinOptions {
          jvmTarget = VERSION_1_8.toString()
          languageVersion = "1.3"
          freeCompilerArgs += "-progressive"
        }
      }

      // activate dependency locking for relevant configurations
      configurations {
        sequenceOf("compile", "runtime", "testCompile", "testRuntime")
          .forEach { scope ->
            getByName("${scope}Classpath") {
              resolutionStrategy.activateDependencyLocking()
            }
          }
      }

      // Test with JUnit 5
      dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:+")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:+")
      }
      tasks.withType<Test> {
        systemProperty("junit.jupiter.execution.parallel.enabled", "false")
        useJUnitPlatform {
          includeEngines("junit-jupiter")
        }
      }

      // Lint Kotlin code
      // TODO: Fails with Kotlin 1.3.20, see https://github.com/jeremymailen/kotlinter-gradle/issues/80
//      apply(plugin = "org.jmailen.kotlinter")
//      configure<KotlinterExtension> {
//        ignoreFailures = false
//        indentSize = 2
//        continuationIndentSize = 4
//        reporters = arrayOf("html", "plain")
//      }
    }

    plugins.withId("info.solidsoft.pitest") {
      configure<PitestPluginExtension> {
        jvmArgs = listOf("-Xmx512m")
        testPlugin = "junit5"
        avoidCallsTo = setOf("kotlin.jvm.internal")
        mutators = setOf("NEW_DEFAULTS")
        targetClasses = setOf("strikt.*")  //by default "${project.group}.*"
        targetTests = setOf("strikt.**.*")
        pitestVersion = "1.4.2"
        threads = System.getenv("PITEST_THREADS")?.toInt() ?:
          Runtime.getRuntime().availableProcessors()
        outputFormats = setOf("XML", "HTML")
      }
    }
  }
}

task("allDependencies") {
  evaluationDependsOnChildren()
  dependsOn(subprojects.map { it.tasks["dependencies"] })
}
