@file:Suppress("KDocMissingDocumentation")

import com.google.protobuf.gradle.ExecutableLocator
import com.google.protobuf.gradle.ProtobufConfigurator
import com.google.protobuf.gradle.ProtobufConfigurator.JavaGenerateProtoTaskCollection
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.net.URL

plugins {
  kotlin("jvm")
  id("published")
  id("com.google.protobuf") version "0.8.16"
}

description = "Extensions for testing code that uses Protobuf / gRPC."

dependencies {
  api(project(":strikt-core"))

  compileOnly("com.google.protobuf:protobuf-java:${property("versions.protobuf")}")
  testImplementation("com.google.protobuf:protobuf-java:${property("versions.protobuf")}")
}

tasks.dokka {
  configuration {
    "https://developers.google.com/protocol-buffers/docs/reference/java/".also {
      externalDocumentationLink {
        url = URL(it)
        packageListUrl = URL(it + "package-list")
      }
    }
  }
}

protobuf {
  protobuf(delegateClosureOf<ProtobufConfigurator> {
    protoc(delegateClosureOf<ExecutableLocator> {
      artifact = "com.google.protobuf:protoc:${property("versions.protobuf")}"
    })
    generateProtoTasks(delegateClosureOf<JavaGenerateProtoTaskCollection> {
      ofSourceSet("test")
    })
  })
}

// This seems to be necessary just to get IntelliJ to notice the proto sources.
// It works fine from the terminal without this. Also IntelliJ picks up main
// protos just fine.

val SourceSet.kotlin
  get() = withConvention(KotlinSourceSet::class) { kotlin }

sourceSets {
  getByName("test") {
    kotlin.srcDirs(
      "src/test/kotlin",
      "$buildDir/generated/source/proto/test/java"
    )
  }
}
