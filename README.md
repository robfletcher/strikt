# Strikt

[![CircleCI](https://circleci.com/gh/robfletcher/strikt/tree/master.svg?style=svg)](https://circleci.com/gh/robfletcher/strikt/tree/master)
[![Download](https://api.bintray.com/packages/robfletcher/maven/strikt-core/images/download.svg) ](https://bintray.com/robfletcher/maven/strikt-core/_latestVersion)

Strikt is an assertion library for Kotlin intended for use with a test runner such as [JUnit](https://junit.org/junit5/) or [Spek](http://spekframework.org/).

The library is at an early stage of development, but usable.
Any suggestions, issue reports, contributions, or feedback are very welcome.

## Installation

Strikt is available from Bintray.

```groovy
repositories { 
  maven { 
    url "https://dl.bintray.com/robfletcher/maven" 
  } 
}

dependencies {
  testCompile "io.strikt:strikt-core:0.4.0"
}
```

## Using Strikt

Please see the [project documentation](https://strikt.io/) and [API docs](https://strikt.io/api/strikt). 
