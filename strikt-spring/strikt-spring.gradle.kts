import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
  id("org.jetbrains.kotlin.plugin.spring") version "1.4.10"
}

dependencies {

  api(project(":strikt-core"))

  implementation(platform("org.springframework.boot:spring-boot-dependencies:2.3.5.RELEASE"))
  compileOnly("org.springframework:spring-test")
  compileOnly("org.springframework:spring-web")
  compileOnly("javax.servlet:javax.servlet-api")

  testImplementation("dev.minutest:minutest:1.11.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-web")
}

//tasks.dokka {
//  configuration {
//    "https://docs.spring.io/spring-framework/docs/current/javadoc-api/".also {
//      externalDocumentationLink {
//        url = URL(it)
//        packageListUrl = URL(it + "package-list")
//      }
//    }
//  }
//}
