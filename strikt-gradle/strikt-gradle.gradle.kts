import org.gradle.util.GradleVersion
import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))

  compileOnly(gradleTestKit())

  testImplementation(gradleTestKit())
  testImplementation("io.mockk:mockk:1.10.0")
  testImplementation("dev.minutest:minutest:1.11.0")
}

//tasks.dokka {
//  configuration {
//    "https://docs.gradle.org/${GradleVersion.current().version}/javadoc/".also {
//      externalDocumentationLink {
//        url = URL(it)
//        packageListUrl = URL(it + "package-list")
//      }
//    }
//  }
//}
