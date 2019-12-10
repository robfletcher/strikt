# Strikt

Strikt is an assertion library for Kotlin intended for use with a test runner such as [JUnit](https://junit.org/junit5/), [Spek](http://spekframework.org/), or [KotlinTest](https://github.com/kotlintest/kotlintest)

Strikt is under development, but 100% usable.
The API may change until a [version 1.0](https://github.com/robfletcher/strikt/milestone/1) is released.
Any suggestions, [issue reports](https://github.com/robfletcher/strikt/issues), [contributions](https://github.com/robfletcher/strikt/pulls), or feedback are very welcome.

## Installation

Strikt is available from JCenter.

```kotlin
repositories {
  jcenter()
}

dependencies {
  testImplementation("io.strikt:strikt-core:<version>")
}
```

See the button below or [releases/latest](https://github.com/robfletcher/strikt/releases/latest) for the current version number.

## Additional Libraries

Strikt has the following additional libraries:

* `strikt-arrow` -- supports data types from the [Arrow](https://arrow-kt.io/) functional programming library.
* `strikt-gradle` -- supports the Gradle build tool.
* `strikt-jackson` -- supports the Jackson JSON library.
* `strikt-java-time` -- supports the JSR-310 `java.time` package.
* `strikt-protobuf` -- supports Protobuf / gRPC.
* `strikt-spring` -- supports the Spring Framework.

Versions are synchronized with the core Strikt library.

To install additional libraries include dependencies in your Gradle build.
For example:

```kotlin
dependencies {
  testImplementation("io.strikt:strikt-java-time:<version>")
}
```

## Bill of Materials

Strikt supplies a BOM that is useful for aligning versions when using more than one Strikt module.

```kotlin
dependencies {
  // BOM dependency
  testImplementation(platform("io.strikt:strikt-bom:<version>"))

  // Versions can be omitted as they are supplied by the BOM
  testImplementation("io.strikt:strikt-jackson")
  testImplementation("io.strikt:strikt-java-time")
  testImplementation("io.strikt:strikt-spring")
}
```

## Using Strikt

Please see the [project documentation](https://strikt.io/) and [API docs](https://strikt.io/api/strikt-core).

## Community

Join the [**#strikt**](https://kotlinlang.slack.com/messages/CAR7KJ96J) channel on the Kotlin Slack.

Follow [**@stri_kt**](https://twitter.com/stri_kt) on Twitter for updates and release notifications.

[![Bintray](https://img.shields.io/badge/dynamic/json.svg?label=latest%20release&url=https%3A%2F%2Fapi.bintray.com%2F%2Fpackages%2Frobfletcher%2Fmaven%2Fstrikt-core%2Fversions%2F_latest&query=name&colorB=0094cd&style=for-the-badge)](https://bintray.com/robfletcher/maven/strikt-core)
[![GitHub Release Date](https://img.shields.io/github/release-date/robfletcher/strikt.svg?style=for-the-badge)](https://github.com/robfletcher/strikt/)
[![license](https://img.shields.io/github/license/robfletcher/strikt.svg?style=for-the-badge&logo=Apache)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub issues](https://img.shields.io/github/issues/robfletcher/strikt.svg?style=for-the-badge&logo=Github)](https://github.com/robfletcher/strikt/issues)
![GitHub top language](https://img.shields.io/github/languages/top/robfletcher/strikt.svg?style=for-the-badge&logo=Kotlin&logoColor=white)
[![Twitter Follow](https://img.shields.io/twitter/follow/stri_kt.svg?style=for-the-badge&label=Twitter&logo=Twitter&logoColor=white)](https://twitter.com/stri_kt)
