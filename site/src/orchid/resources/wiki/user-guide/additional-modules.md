---
---

# Additional Modules

In addition to the core functionality provided by the `strikt-core` module, Strikt has the following optional modules:

## Java Time

Extensions for assertions and traversals on types from the `java.time` package.
See the {{ anchor('API docs', 'strikt.java-time') }}.

Add the following to your dependencies:

```kotlin
testCompile("io.strikt:strikt-java-time:{{ site.version }}")
``` 

## Protobuf

Extensions for testing code that uses Protobuf / gRPC.
See the {{ anchor('API docs', 'strikt.protobuf') }}.

Add the following to your dependencies:

```kotlin
testCompile "io.strikt:strikt-protobuf:{{ site.version }}"
``` 
