import org.ajoberstar.gradle.git.ghpages.GithubPagesPluginExtension.DestinationCopySpec
import org.apache.tools.ant.filters.ConcatFilter
import org.apache.tools.ant.filters.ReplaceTokens
import org.jbake.gradle.JBakeTask

plugins {
  id("org.ajoberstar.github-pages")
  id("org.jbake.site") version "1.2.0"
}

configurations.jbake.resolutionStrategy {
  activateDependencyLocking()
}

dependencies {
  jbake("com.orientechnologies:orientdb-core:2.2.34+")
}

jbake {
  flexmarkVersion = "0.34.28"
}

val copyApiDocs = task<Copy>("copyApiDocs") {
  rootProject
    .allprojects
    .mapNotNull { it -> it.tasks.findByPath("${it.path}:dokka") }
    .forEach { dokkaTask ->
      // add JBake header to *.html
      from(dokkaTask.outputs) {
        include("**/*.html")
        filter(
          ConcatFilter::class,
          "prepend" to file("$projectDir/api-header.txt")
        )
      }
      from(dokkaTask.outputs) {
        include("**/package-list")
      }
    }
  into("$projectDir/src/jbake/content/api/")
  // filter out everything except HTML body as it will be wrapped by JBake template
  filter { line ->
    val strip = listOf(
      "<HTML>",
      "<HEAD>",
      "<meta",
      "<title>",
      "<link",
      "</HEAD>",
      "<BODY>",
      "</BODY>",
      "</HTML>"
    )
    if (strip.any { line.startsWith(it) }) {
      ""
    } else {
      line
    }
  }
}

tasks.withType<JBakeTask> {
  dependsOn(copyApiDocs)
}

tasks.getByName("clean").doFirst {
  delete("$projectDir/src/jbake/content/api/")
}

githubPages {
  // authentication is via GRGIT_USER environment variable set to GitHub API key
  setRepoUri("https://github.com/robfletcher/strikt.git")
  targetBranch = "gh-pages"
  pages(delegateClosureOf<DestinationCopySpec> {
    // necessary as `pages` relies on Groovy @Delegate which doesn't work from Kotlin
    realSpec.apply {
      val bakeTask = tasks.getByName("bake")
      // everything that's NOT *.html gets copied directly
      from(bakeTask.outputs) {
        exclude("**/*.html")
      }
      // everything that is *.html is filtered for @version@
      from(bakeTask.outputs) {
        include("**/*.html")
        val version = (System.getenv("CIRCLE_TAG") ?: "+").removePrefix("v")
        filter(
          ReplaceTokens::class,
          "tokens" to mapOf("version" to version)
        )
      }
      // include CircleCI config or it will try to build gh-pages branch
      from(rootDir) {
        include(".circleci/**/*")
      }
    }
  })
}
