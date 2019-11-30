import org.gradle.util.GradleVersion
import java.net.URL

plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly(gradleTestKit())

  testImplementation(gradleTestKit())
  testImplementation("io.mockk:mockk:1.9.3")
  testImplementation("dev.minutest:minutest:1.10.0")
}

tasks.dokka {
  externalDocumentationLink {
    url = URL("https://docs.gradle.org/${GradleVersion.current().version}/javadoc/")
  }
}
