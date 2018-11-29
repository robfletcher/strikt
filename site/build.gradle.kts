@file:Suppress("KDocMissingDocumentation")

import com.eden.orchid.gradle.OrchidPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
This project builds the Strikt docs with Orchid.

Commands:
    gradle :site:orchidServe
        build the site and serve it locally on http://localhost:8080. Changes to
        site content will rebuild the site.
    gradle :site:orchidDeploy -Penv=prod
        build the site and deploy it to Github Pages. Requires an API token with
        push access to the repo, set as `github_token` in Gradle properties, or
        a `GITHUB_TOKEN` environment variable. The `env` project property will
        set the appropriate site base URL.
*/

plugins {
  id("nebula.kotlin")
  id("com.eden.orchidPlugin") version "0.14.0"
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx")
  maven(url = "https://dl.bintray.com/javaeden/Orchid/")
  maven(url = "https://dl.bintray.com/javaeden/Eden/")
  maven(url = "https://jitpack.io")
}

configurations {
  sequenceOf("orchidCompile", "orchidRuntime")
    .forEach { scope ->
      getByName("${scope}Classpath") {
        resolutionStrategy.activateDependencyLocking()
      }
    }
}

dependencies {
  orchidCompile("io.github.javaeden.orchid:OrchidCore:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidCore:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidPages:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidSearch:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidWiki:0.14.0")
  orchidRuntime("io.github.javaeden.orchid:OrchidChangelog:0.14.0")
}

project.version = "${project.version}"

orchid {
  theme = "StriktTheme"

  if (project.hasProperty("env") && project.property("env") == "prod") {
    baseUrl = "https://strikt.io/"
    environment = "prod"
  } else {
    baseUrl = "http://localhost:8080"
    environment = "debug"
  }

  args = listOf(
    "githubToken ${if (project.hasProperty("github_token")) project.property("github_token") else System.getenv(
      "GITHUB_TOKEN"
    )}"
  )
}

val compileOrchidKotlin by tasks.getting(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}
