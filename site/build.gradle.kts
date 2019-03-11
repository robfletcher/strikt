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
  id("com.eden.orchidPlugin") version "0.16.4"
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx")
}

open class OrchidAlignmentRule : ComponentMetadataRule {
  override fun execute(ctx: ComponentMetadataContext) {
    ctx.details.run {
      if (id.group.startsWith("io.github.javaeden.orchid")) {
        // declare that Jackson modules all belong to the Jackson virtual platform
        belongsTo("io.github.javaeden.orchid:orchid-platform:${id.version}")
      }
    }
  }
}

dependencies {
  components.all(OrchidAlignmentRule::class.java)
  orchidCompile("io.github.javaeden.orchid:OrchidCore:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidDocs:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:+")
  orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:+")
}

project.version = "${project.version}"

orchid {
  theme = "StriktTheme"
  baseUrl = "https://strikt.io/"

  if (project.hasProperty("env") && project.property("env") == "prod") {
    environment = "prod"
  } else {
    environment = "debug"
  }

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
