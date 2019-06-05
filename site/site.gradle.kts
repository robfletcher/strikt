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
  id("com.eden.orchidPlugin") version "0.16.10"
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
  orchidCompile("io.github.javaeden.orchid:OrchidCore:0.16.10")
  orchidRuntime("io.github.javaeden.orchid:OrchidDocs:0.16.10")
  orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:0.16.10")
  orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:0.16.10")
}

project.version = "${project.version}"

orchid {
  evaluationDependsOn(":strikt-core")
  evaluationDependsOn(":strikt-gradle")
  evaluationDependsOn(":strikt-jackson")
  evaluationDependsOn(":strikt-java-time")
  evaluationDependsOn(":strikt-okhttp")
  evaluationDependsOn(":strikt-protobuf")
  evaluationDependsOn(":strikt-spring")

  theme = "StriktTheme"
  baseUrl = "https://strikt.io/"

  if (project.hasProperty("env") && project.property("env") == "prod") {
    environment = "prod"
  } else {
    environment = "debug"
  }

  args = listOf(
    "--kotlindocClasspath",
    listOf(
      ":strikt-core",
      ":strikt-jackson",
      ":strikt-java-time",
      ":strikt-protobuf",
      ":strikt-spring"
    )
      .joinToString(File.pathSeparator) {
        project(it).sourceSets["main"].compileClasspath.asPath
      }
  )

  githubToken = if (project.hasProperty("github_token")) {
    project.property("github_token").toString()
  } else {
    System.getenv("GITHUB_TOKEN")
  }
}

val compileOrchidKotlin by tasks.getting(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}
