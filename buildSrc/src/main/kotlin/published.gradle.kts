import com.jfrog.bintray.gradle.BintrayExtension

plugins {
  id("org.jetbrains.dokka")
  id("nebula.maven-publish")
  id("nebula.source-jar")
  id("nebula.nebula-bintray-publishing")
}

plugins.withId("kotlin") {
  tasks.withType<Javadoc> {
    enabled = false
  }

  tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
    configuration {
      jdkVersion = 9
    }
  }

  val dokkaJar = task<Jar>("dokkaJar") {
    group = "build"
    description = "Assembles Javadoc jar from Dokka API docs"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
  }

  publishing {
    publications {
      getByName<MavenPublication>("nebula") {
        artifact(dokkaJar)
      }
    }
  }
}

publishing {
  publications {
    getByName<MavenPublication>("nebula") {
      pom {
        url.set("https://strikt.io/")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("robfletcher")
            name.set("Rob Fletcher")
            email.set("rob at freeside.co")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/robfletcher/strikt.git")
          developerConnection
            .set("scm:git:ssh://github.com/robfletcher/strikt.git")
          url.set("http://github.com/robfletcher/strikt/")
        }
      }
    }
  }
}

bintray {
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
