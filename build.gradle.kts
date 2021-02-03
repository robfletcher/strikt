import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension
import kotlin.text.RegexOption.*
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  kotlin("jvm") version "1.4.21-2" apply false
  id("nebula.release") version "15.0.1"
  id("org.jmailen.kotlinter") version "3.3.0" apply false
  id("info.solidsoft.pitest") version "1.5.2" apply false
  id("com.adarshr.test-logger") version "2.1.1" apply false
  id("com.github.ben-manes.versions") version "0.36.0"
}

fun ModuleComponentIdentifier.isNonStable() =
  version.contains(Regex("""-(m|eap|rc|alpha|beta)(-?[\d-]+)?$""", IGNORE_CASE))

allprojects {
  group = "io.strikt"

  dependencyLocking {
    lockAllConfigurations()
  }

  configurations.all {
    @Suppress("ObjectLiteralToLambda")
    resolutionStrategy.componentSelection.all(object : Action<ComponentSelection> {
      @Suppress("UnstableApiUsage")
      @Mutate
      override fun execute(selection: ComponentSelection) {
        val isChanging = selection.metadata?.isChanging ?: false
        val isRelease = selection.metadata?.status == "release"
        val isCandidate = selection.candidate.isNonStable()
        if (isChanging || !isRelease || isCandidate) {
          selection.reject("Non-release versions are not allowed.")
        }
      }
    })
  }
}

subprojects {
  apply(plugin = "nebula.release")

  repositories {
    jcenter()
    mavenCentral() // needed for dependencyUpdates to work with arrow which has no metadata on jcenter
  }

//  configurations.all {
//    resolutionStrategy.eachDependency {
//      if (requested.group == "org.jetbrains.kotlin") {
//        useVersion("1.4.30")
//      }
//    }
//  }

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

      // Test with JUnit 5
      dependencies {
        "implementation"(platform("org.jetbrains.kotlin:kotlin-bom:1.4.+"))
        "implementation"(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.4.+"))
        "testImplementation"(platform("org.junit:junit-bom:+"))
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

tasks.withType<DependencyUpdatesTask> {
  revision = "release"
  checkConstraints = true
  gradleReleaseChannel = "current"
  checkForGradleUpdate = true
  rejectVersionIf {
    candidate.isNonStable()
  }
}
