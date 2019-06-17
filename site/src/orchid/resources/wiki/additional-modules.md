---
---

# Additional Modules

In addition to the core functionality provided by the `strikt-core` module, Strikt has the following optional modules:

## Gradle

Extensions for assertions and travels on [Gradle](https://github.com/gradle/gradle)'s test kit.
See the {{ anchor('API docs', 'strikt.gradle.testkit') }}.

Add the following to your dependencies:

```kotlin
testImplementation("io.strikt:strikt-gradle:{{ site.version }}")
```

## Jackson

Extensions for assertions and traversals on types [Jackson](https://github.com/FasterXML/jackson)'s `JsonNode` and sub-types.
See the {{ anchor('API docs', 'strikt.jackson') }}.

Add the following to your dependencies:

```kotlin
testImplementation("io.strikt:strikt-jackson:{{ site.version }}")
``` 

## Java Time

Extensions for assertions and traversals on types from the `java.time` package.
See the {{ anchor('API docs', 'strikt.time') }}.

Add the following to your dependencies:

```kotlin
testImplementation("io.strikt:strikt-java-time:{{ site.version }}")
``` 

## Protobuf

Extensions for testing code that uses Protobuf / gRPC.
See the {{ anchor('API docs', 'strikt.protobuf') }}.

Add the following to your dependencies:

```kotlin
testImplementation("io.strikt:strikt-protobuf:{{ site.version }}")
``` 

## Spring

Extensions for testing code that uses the Spring Framework.
See the {{ anchor('API docs', 'strikt.spring') }}.

Add the following to your dependencies:

```kotlin
testImplementation("io.strikt:strikt-spring:{{ site.version }}")
``` 

## Using the Bill of Materials (BOM)

If you're using multiple Strikt modules (and Gradle >= 5.0) you can import Strikt's BOM and then omit versions for individual modules.
For example:

```kotlin
dependencies {
  testImplementation(platform("io.strikt:strikt-bom:{{ site.version }}"))
  testImplementation("io.strikt:strikt-jackson")
  testImplementation("io.strikt:strikt-java-time")
}
```
