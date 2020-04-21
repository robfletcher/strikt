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
  id("com.eden.orchidPlugin") version "0.19.0"
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx")
  maven(url="https://jitpack.io")
}

dependencies {
  val orchidVersion = "0.20.0"
  orchidImplementation("io.github.javaeden.orchid:OrchidCore:$orchidVersion")
  orchidRuntimeOnly("io.github.javaeden.orchid:OrchidDocs:$orchidVersion")
  orchidRuntimeOnly("io.github.javaeden.orchid:OrchidPluginDocs:$orchidVersion")
  orchidRuntimeOnly("io.github.javaeden.orchid:OrchidKotlindoc:$orchidVersion")
  orchidRuntimeOnly("io.github.javaeden.orchid:OrchidGithub:$orchidVersion")
}

project.version = "${project.version}"

orchid {
  theme = "StriktTheme"
  baseUrl = "https://strikt.io/"

  environment = if (findProperty("env") == "prod") { "prod" } else { "debug" }
  args = listOf("--experimentalSourceDoc")

  githubToken = if (hasProperty("github_token")) {
    property("github_token").toString()
  } else {
    System.getenv("GITHUB_TOKEN")
  }
}

val compileOrchidKotlin by tasks.getting(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}
