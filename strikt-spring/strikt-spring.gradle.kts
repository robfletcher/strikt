import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.published)
  alias(libs.plugins.kotlin.spring)
}

description = "Extensions for testing code that uses the Spring Framework."

dependencies {

  api(project(":strikt-core"))

  implementation(platform(libs.spring.boot))
  compileOnly(libs.spring.test)
  compileOnly(libs.spring.web)
  compileOnly(libs.jakarta.servlet.api)

  testImplementation(libs.minutest)
  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.spring.boot.starter.web)
}

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets {
    configureEach {
      "https://docs.spring.io/spring-framework/docs/current/javadoc-api/".also {
        externalDocumentationLink {
          url.set(URI(it).toURL())
          packageListUrl.set(URI("${it}package-list").toURL())
        }
      }
    }
  }
}
