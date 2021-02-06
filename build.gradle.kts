import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension
import kotlin.text.RegexOption.*
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  kotlin("jvm") version "1.4.30" apply false
  id("nebula.release") version "15.0.1"
  id("org.jmailen.kotlinter") version "3.3.0" apply false
  id("info.solidsoft.pitest") version "1.5.2" apply false
  id("com.adarshr.test-logger") version "2.1.1" apply false
  id("com.github.ben-manes.versions") version "0.36.0"
}

allprojects {
  group = "io.strikt"

  apply<com.github.benmanes.gradle.versions.VersionsPlugin>()

  configurations.all {
    resolutionStrategy.eachDependency {
      if (requested.group == "org.jetbrains.kotlin") {
        useVersion("${property("versions.kotlin")}")
      }
    }
  }
}

subprojects {
  apply(plugin = "nebula.release")

  repositories {
    mavenCentral()
    jcenter() // TODO: required until filepeek is on Maven Central
  }

  afterEvaluate {
    plugins.withId("kotlin") {
      configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_8
      }

      tasks.withType<KotlinCompile> {
        kotlinOptions {
          jvmTarget = VERSION_1_8.toString()
          languageVersion = "1.4"
          javaParameters = true
          freeCompilerArgs = listOf("-Xjvm-default=all")
          allWarningsAsErrors = true
        }
      }

      dependencies {
        "implementation"(platform("org.jetbrains.kotlin:kotlin-bom:${property("versions.kotlin")}"))
        "implementation"(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:${property("versions.kotlinx-coroutines")}"))

        "testImplementation"(platform("org.junit:junit-bom:${property("versions.junit")}"))
        "testImplementation"("org.junit.jupiter:junit-jupiter-api")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
      }

      // Test with JUnit 5
      tasks.withType<Test> {
        systemProperty("junit.jupiter.execution.parallel.enabled", "false")
        useJUnitPlatform {
          includeEngines("junit-jupiter")
        }
      }

      // Lint Kotlin code
      apply(plugin = "org.jmailen.kotlinter")
      configure<KotlinterExtension> {
        ignoreFailures = true
        indentSize = 2
        reporters = arrayOf("html", "plain")
      }
    }

    plugins.withId("info.solidsoft.pitest") {
      configure<PitestPluginExtension> {
        junit5PluginVersion.set("0.12")
        avoidCallsTo.set(setOf("kotlin.jvm.internal"))
        targetClasses.set(setOf("strikt.*"))  // by default "${project.group}.*"
        targetTests.set(setOf("strikt.**.*"))
        pitestVersion.set("1.6.2")
        threads.set(
          System.getenv("PITEST_THREADS")?.toInt()
            ?: Runtime.getRuntime().availableProcessors()
        )
        outputFormats.set(setOf("XML", "HTML"))
      }
    }
  }

  apply(plugin = "com.adarshr.test-logger")
  configure<TestLoggerExtension> {
    theme = MOCHA_PARALLEL
    showSimpleNames = true
  }
}

fun ModuleComponentIdentifier.isNonStable() =
  version.contains(Regex("""-(m|eap|rc|alpha|beta)(-?[\d-]+)?$""", IGNORE_CASE))

tasks.withType<DependencyUpdatesTask> {
  revision = "release"
  checkConstraints = true
  gradleReleaseChannel = "current"
  checkForGradleUpdate = true
  rejectVersionIf {
    candidate.isNonStable()
  }
}
