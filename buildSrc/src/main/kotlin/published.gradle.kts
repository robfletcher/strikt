plugins {
  id("org.jetbrains.dokka")
  id("nebula.maven-publish")
  id("nebula.source-jar")
  signing
}

publishing {
  repositories {
    maven {
      name = "nexus"
      url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
      credentials(PasswordCredentials::class)
    }
  }

  publications {
    getByName<MavenPublication>("nebula") {
      pom {
        description.set("An assertion library for Kotlin")
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
        inceptionYear.set("2017")
        scm {
          connection.set("scm:git:git://github.com/robfletcher/strikt.git")
          developerConnection.set("scm:git:ssh://github.com/robfletcher/strikt.git")
          url.set("http://github.com/robfletcher/strikt/")
        }
        issueManagement {
          system.set("GitHub Issues")
          url.set("https://github.com/robfletcher/strikt/issues")
        }
      }
    }
  }
}

signing {
  sign(publishing.publications["nebula"])
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
