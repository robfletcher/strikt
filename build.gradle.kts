import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension

plugins {
  kotlin("jvm") version "1.4.10" apply false
  id("nebula.release") version "15.0.1"
  id("org.jmailen.kotlinter") version "3.0.2" apply false
  id("info.solidsoft.pitest") version "1.5.0" apply false
  id("com.github.ben-manes.versions") version "0.31.0"
  id("com.adarshr.test-logger") version "2.1.0" apply false
  id("com.autonomousapps.dependency-analysis") version "0.59.0"
}

buildscript {
  configurations.maybeCreate("pitest")
  dependencies {
    "pitest"("org.pitest:pitest-junit5-plugin:0.12")
  }
}

allprojects {
  group = "io.strikt"
}

subprojects {
  apply(plugin = "nebula.release")

  repositories {
    jcenter()
    mavenCentral() // needed for dependencyUpdates to work with arrow which has no metadata on jcenter
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
          freeCompilerArgs = listOf("-Xjvm-default=all")
        }
      }

      // Test with JUnit 5
      dependencies {
        "implementation"(platform("org.jetbrains.kotlin:kotlin-bom:1.4.10"))
        "implementation"(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.3.9"))
        "testImplementation"(platform("org.junit:junit-bom:5.6.2"))
        "testImplementation"("org.junit.jupiter:junit-jupiter-api")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
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
        ignoreFailures = true
        indentSize = 2
        reporters = arrayOf("html", "plain")
      }
    }

    plugins.withId("info.solidsoft.pitest") {
      configure<PitestPluginExtension> {
        jvmArgs.set(listOf("-Xmx512m"))
        testPlugin.set("junit5")
        avoidCallsTo.set(setOf("kotlin.jvm.internal"))
        mutators.set(setOf("NEW_DEFAULTS"))
        targetClasses.set(setOf("strikt.*"))  //by default "${project.group}.*"
        targetTests.set(setOf("strikt.**.*"))
        pitestVersion.set("1.5.1")
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

tasks.withType<DependencyUpdatesTask> {
  revision = "release"
  checkConstraints = true
  gradleReleaseChannel = "current"
  rejectVersionIf {
    candidate.version.contains(Regex("""-(M|eap|rc|RC|alpha|beta)(-?[\d-]+)?$"""))
  }
}
