@file:Suppress("KDocMissingDocumentation")

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
  id("com.eden.orchidPlugin") version "0.12.8"
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
  orchidCompile("io.github.javaeden.orchid:OrchidCore:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidCore:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidPages:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidSearch:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidWiki:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidChangelog:+")
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
