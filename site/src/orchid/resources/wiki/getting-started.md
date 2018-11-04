---
---

# Getting Started

Strikt does not depend on any particular test runner.
It can be used with JUnit, Spek or any other runner that supports tests written in Kotlin.

## Installation

Strikt is available from JCenter.
Add the following to your `build.gradle` or `build.gradle.kts`.

```kotlin
repositories {
  jcenter()
}

dependencies {
  testCompile("io.strikt:strikt-core:{{ site.version }}")
}
```

## Importing the Strikt API

Add the following imports to your test:

```kotlin
import strikt.api.*
import strikt.assertions.*
```

The `strikt.api` package contains top level functions such as `expect`, `expectThat`, and `expectThrows` that you will use to create assertions as well as the API classes you will interact with if you decide to implement your own assertion functions.
The `strikt.assertions` package contains the standard library of assertion functions.

