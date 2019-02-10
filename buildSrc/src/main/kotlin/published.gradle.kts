import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
  id("org.jetbrains.dokka")
  id("nebula.maven-publish")
  id("nebula.source-jar")
  id("nebula.nebula-bintray-publishing")
}

tasks.withType<Javadoc> {
  enabled = false
}

val dokka by tasks.getting(DokkaTask::class) {
  outputFormat = "html"
  outputDirectory = "$buildDir/javadoc"
  jdkVersion = 9
}

val dokkaJar = task<Jar>("dokkaJar") {
  group = "build"
  description = "Assembles Javadoc jar from Dokka API docs"
  archiveClassifier.set("javadoc")
  from(tasks.named("dokka"))
}

configure<PublishingExtension>() {
  publications {
    getByName<MavenPublication>("nebula") {
      artifact(dokkaJar)
    }
  }
}

configure<BintrayExtension> {
  user = System.getenv("BINTRAY_USER")
  key = System.getenv("BINTRAY_KEY")
  pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
    userOrg = "robfletcher"
    repo = "maven"
    websiteUrl = "https://strikt.io/"
    issueTrackerUrl = "https://github.com/robfletcher/strikt/issues"
    vcsUrl = "https://github.com/robfletcher/strikt.git"
    setLicenses("Apache-2.0")
    setLabels("testing", "kotlin")
  })
}
