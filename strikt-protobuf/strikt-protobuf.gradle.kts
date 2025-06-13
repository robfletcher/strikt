@file:Suppress("KDocMissingDocumentation")

import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.published)
  alias(libs.plugins.protobuf)
}

description = "Extensions for testing code that uses Protobuf / gRPC."

dependencies {
  api(project(":strikt-core"))

  compileOnly(libs.protobuf)
  testImplementation(libs.protobuf)
}

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets {
    configureEach {
      "https://developers.google.com/protocol-buffers/docs/reference/java/".also {
        externalDocumentationLink {
          url.set(URI(it).toURL())
          packageListUrl.set(URI("${it}package-list").toURL())
        }
      }
    }
  }
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.java.get()}"
  }
  generateProtoTasks {
    ofSourceSet("test")
  }
}

sourceSets {
  getByName("test") {
    kotlin.srcDirs(
      "src/test/kotlin",
      "$buildFile/generated/source/proto/test/java"
    )
  }
}
