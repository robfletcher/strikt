import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("org.jetbrains.kotlin.plugin.spring")
}

description = "Extensions for testing code that uses the Spring Framework."

dependencies {

  api(project(":strikt-core"))

  implementation(platform("org.springframework.boot:spring-boot-dependencies:${property("versions.spring-boot")}"))
  compileOnly("org.springframework:spring-test")
  compileOnly("org.springframework:spring-web")
  compileOnly("jakarta.servlet:jakarta.servlet-api")

  testImplementation("dev.minutest:minutest:${property("versions.minutest")}")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets {
    configureEach {
      "https://docs.spring.io/spring-framework/docs/current/javadoc-api/".also {
        externalDocumentationLink {
          url.set(URL(it))
          packageListUrl.set(URL("${it}package-list"))
        }
      }
    }
  }
}

afterEvaluate {
  plugins.withId("kotlin") {
    configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
      }
    }
  }
}
