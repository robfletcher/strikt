import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import info.solidsoft.gradle.pitest.PitestPluginExtension
import io.codearte.gradle.nexus.NexusStagingExtension
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension
import kotlin.text.RegexOption.IGNORE_CASE

plugins {
  kotlin("jvm") version "1.5.30" apply false
  id("io.codearte.nexus-staging") version "0.30.0"
  id("org.jmailen.kotlinter") version "3.5.0" apply false
  id("info.solidsoft.pitest") version "1.6.0" apply false
  id("com.adarshr.test-logger") version "3.0.0" apply false
  id("com.github.ben-manes.versions") version "0.39.0"
  id("org.jetbrains.dokka")
}

repositories {
  mavenCentral()
  // needed for dokka plugin, feels like this belongs in published.gradle.kts but it doesn't work there
  maven {
    url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
  }
}

allprojects {
  group = "io.strikt"

  configurations.all {
    resolutionStrategy.eachDependency {
      if (requested.group == "org.jetbrains.kotlin") {
        useVersion("${property("versions.kotlin")}")
      }
    }
  }
}

subprojects {
  repositories {
    mavenCentral()
    // needed for dokka plugin, feels like this belongs in published.gradle.kts but it doesn't work there
    maven {
      url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }
  }

  afterEvaluate {
    plugins.withId("kotlin") {
      configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_8
      }

      tasks.withType<KotlinCompile> {
        kotlinOptions {
          jvmTarget = VERSION_1_8.toString()
          languageVersion = "1.5"
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

configure<NexusStagingExtension> {
  stagingProfileId = "3fc70880a122f"
}

// Dependency updates configuration
fun ModuleComponentIdentifier.isNonStable() =
  version.contains(Regex("""-(m|eap|rc|alpha|beta|b)(-?[\d-]+)?$""", IGNORE_CASE))

tasks.withType<DependencyUpdatesTask> {
  revision = "release"
  checkConstraints = true
  gradleReleaseChannel = "current"
  checkForGradleUpdate = true
  rejectVersionIf {
    candidate.isNonStable()
  }
}
