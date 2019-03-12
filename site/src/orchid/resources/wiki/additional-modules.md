---
---

# Additional Modules

In addition to the core functionality provided by the `strikt-core` module, Strikt has the following optional modules:

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
