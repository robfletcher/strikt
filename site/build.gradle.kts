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
  id("com.eden.orchidPlugin") version "0.12.6"
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx")
  maven(url = "https://dl.bintray.com/javaeden/Orchid/")
  maven(url = "https://dl.bintray.com/javaeden/Eden/")
  maven(url = "https://jitpack.io")
}

dependencies {
  val orchid_version = "0.12.6"
  orchidCompile("io.github.javaeden.orchid:OrchidCore:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidCore:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidPages:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidSearch:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidWiki:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidChangelog:$orchid_version")
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
    "githubToken ${if(project.hasProperty("github_token")) project.property("github_token") else System.getenv("GITHUB_TOKEN")}"
  )
}

val compileOrchidKotlin by tasks.getting(KotlinCompile::class) {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
}
