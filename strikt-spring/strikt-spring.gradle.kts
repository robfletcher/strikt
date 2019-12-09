import java.net.URL

plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
  id("kotlin-spring")
}

dependencies {

  api(project(":strikt-core"))

  implementation(platform("org.springframework.boot:spring-boot-dependencies:2.2.2.RELEASE"))
  compileOnly("org.springframework:spring-test")
  compileOnly("org.springframework:spring-web")
  compileOnly("javax.servlet:javax.servlet-api")

  testImplementation("dev.minutest:minutest:1.10.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.dokka {
  configuration {
    externalDocumentationLink {
      url =
        URL("https://docs.spring.io/spring-framework/docs/current/javadoc-api/")
    }
  }
}
