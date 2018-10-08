import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.JavaVersion.VERSION_1_6
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension

plugins {
  id("nebula.release") version "7.0.1"
  id("nebula.kotlin") version "1.2.71" apply false
  id("org.jmailen.kotlinter") version "1.17.0" apply false
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

  // prevent --write-locks from selecting candidates, milestones, etc.
  configurations.all {
    resolutionStrategy {
      componentSelection.all {
        val rejected = listOf("alpha", "beta", "rc", "cr", "m", "pr")
          .any { qualifier ->
            candidate.version.matches("(?i).*[.-]$qualifier[.\\d-]*".toRegex())
          }
        if (rejected) {
          reject("Release candidate")
        }
      }
    }
  }
}

subprojects {
  apply(plugin = "nebula.release")

  repositories {
    jcenter()
  }

  afterEvaluate {
    plugins.withId("kotlin") {
      configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_6
      }

      tasks.withType<KotlinCompile> {
        kotlinOptions {
          languageVersion = "1.2"
          freeCompilerArgs += "-Xprogressive"
        }
      }

      // build library code for JDK 1.6
      val compileKotlin by tasks.getting(KotlinCompile::class) {
        kotlinOptions {
          jvmTarget = VERSION_1_6.toString()
        }
      }

      // build test code for JDK 1.8
      val compileTestKotlin by tasks.getting(KotlinCompile::class) {
        kotlinOptions {
          jvmTarget = VERSION_1_8.toString()
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
      apply(plugin = "org.jmailen.kotlinter")
      configure<KotlinterExtension> {
        ignoreFailures = false
        indentSize = 2
        continuationIndentSize = 4
        reporters = arrayOf("html", "plain")
      }
    }

    plugins.withId("info.solidsoft.pitest") {
      configure<PitestPluginExtension> {
        testPlugin = "junit5"
        avoidCallsTo = setOf("kotlin.jvm.internal")
        mutators = setOf("NEW_DEFAULTS")
        targetClasses = setOf("strikt.*")  //by default "${project.group}.*"
        targetTests = setOf("strikt.**.*")
        pitestVersion = "1.4.2"
        threads = Runtime.getRuntime().availableProcessors()
        outputFormats = setOf("XML", "HTML")
      }
    }
  }
}

