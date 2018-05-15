![Strikt](img/logo.png)

[![CircleCI](https://circleci.com/gh/robfletcher/strikt/tree/master.svg?style=svg)](https://circleci.com/gh/robfletcher/strikt/tree/master)
[![Download](https://api.bintray.com/packages/robfletcher/maven/strikt-core/images/download.svg) ](https://bintray.com/robfletcher/maven/strikt-core/_latestVersion)

Strikt is an assertion library for Kotlin intended for use with a test runner such as [JUnit](https://junit.org/junit5/) or [Spek](http://spekframework.org/).
It's very much inspired by [AssertJ](https://joel-costigliola.github.io/assertj/), [Atrium](https://robstoll.github.io/atrium/) and [Hamkrest](https://github.com/npryce/hamkrest).
However, none of those provided exactly what I wanted so I decided to create my own assertions library.

The design goals I had in mind were:

- An assertion API that takes advantage of Kotlin's strong type system.
- Easy "soft assertions" out of the box.
- A simple API for composing custom assertions.
- Legible syntax that an IDE can help with.
- Use Kotlin's nice language features without getting overly-clever (torturing everything into an infix function syntax, or trying to recreate [Spock](http://spockframework.org/)'s assertion syntax in a language that can't really do it, for example).
- A rich selection of assertions that apply to common types without a tangled hierarchy of classes and self-referential generic types, (it turns out Kotlin's extension functions make this pretty easy to accomplish).
- Simple setup -- one dependency, one (okay, two) imports and you're up and running.

## Installation

Strikt is available from JCenter.
Add the following to your `build.gradle`.

```groovy
repositories { 
  maven { 
    url "https://dl.bintray.com/robfletcher/maven" 
  } 
}

dependencies {
  testCompile "io.github.robfletcher.strikt:strikt-core:0.3.0"
}
```
