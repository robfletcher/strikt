import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("nebula.kotlin")
  id("com.eden.orchidPlugin") version "0.12.3"
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx")
  maven(url = "https://dl.bintray.com/javaeden/Orchid/")
  maven(url = "https://dl.bintray.com/javaeden/Eden/")
  maven(url = "https://jitpack.io")
}

dependencies {
  val orchid_version = "0.12.3"
  orchidCompile("io.github.javaeden.orchid:OrchidCore:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidCore:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidBsDoc:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidPages:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidSearch:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:$orchid_version")
  orchidRuntime("io.github.javaeden.orchid:OrchidWiki:$orchid_version")
}

project.version = "${project.version}"

orchid {
  theme = "Strikt"

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
