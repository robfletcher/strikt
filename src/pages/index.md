[![CircleCI](https://circleci.com/gh/robfletcher/kirk/tree/master.svg?style=svg)](https://circleci.com/gh/robfletcher/kirk/tree/master)
[API docs](api/kirk/)

Kirk is an assertion library for Kotlin intended for use with a test runner such as [JUnit](https://junit.org/junit5/) or [Spek](http://spekframework.org/).
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

Currently Kirk is hosted on Bintray.
Add the following to your `build.gradle`.

```groovy
repositories { 
  maven { url "https://dl.bintray.com/robfletcher/maven" } 
}

testCompile "io.github.robfletcher.kirk:kirk-core:0.1.0"
```

## Assertion styles

Two different styles of assertion -- chained and block -- are supported for different use-cases.
You can mix and match both in the same test and even nest chained assertions inside block assertions.

### Chained assertions

Chained assertions use a fluent API.
They fail fast.
That is, the first assertion that fails breaks the chain and further assertions are not evaluated.

Each assertion in the chain returns an `Assertion` object that supports further assertions.

```kotlin
val subject = "covfefe"
expect(subject)
  .isA<String>()
  .hasLength(1)
  .isUpperCase()
```

Produces the output: 

```
✔ "covfefe" is a java.lang.String
✘ "covfefe" has length 1
```

Notice that the `isUpperCase()` assertion is not applied as the earlier `hasLength(1)` assertion failed.

### Block assertions

Block assertions are declared in a block whose receiver is an assertion on a target object.
Block assertions do _not_ fail fast.
That is, all assertions in the block are evaluated and the result of the "compound" assertion will include results for all the assertions made in the block.

```kotlin
val subject = "covfefe"
expect(subject) {
  isA<String>()
  hasLength(1)
  isUpperCase()
}
```

Produces the output:

```
✔ "covfefe" is a java.lang.String
✘ "covfefe" has length 1
✘ "covfefe" is upper case
```

All assertions are applied and since two fail there are two errors logged.

### Chained assertions inside block assertions

Chained assertions inside a block _will_ still fail fast but will not prevent other assertions in the block from being evaluated.

```kotlin
val subject = 1L
expect(subject) {
  lessThan(1).isA<Int>()
  greaterThan(1)
}
```

Produces the output:

```
✘ 1 is less than 1
✘ 1 is greater than 1
```

Note the `isA<Int>` assertion (that would have failed) was not evaluated since it was chained after `lessThan(1)` which failed.
The `greaterThan(1)` assertion _was_ evaluated since it was not part of the same chain.

## Flow typed assertions

Chained assertions return an `Assertion<T>` object with a generic type representing the (declared) type of the assertion subject.
Some assertions will return a _different_ type to the one they were called on.
For example, if the subject of an assertion is a nullable type (in other words it's an `Assertion<T?>`) the assertion methods `isNull()` and `isNotNull()` are available.
The return type of `isNotNull()` is `Assertion<T>` because we now _know_ the subject is not null.
You will find IDE code-completion will no longer offer the `isNull()` and `isNotNull()` assertion methods.

Another example comes when testing values with broad types and making assertions about their specific runtime type.
For example:

```kotlin
val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
expect(subject.get("count")).isA<Number>().isGreaterThan(0)
expect(subject.get("name")).isA<String>().hasLength(3)
```

The return type of the subject map's `get()` method is `Any` but using the "down-cast" assertion `isA<T>()` we can both assert the type of the value and -- because the compiler now knows it is dealing with an `Assertion<String>` or an `Assertion<Number>` -- we can use more specialized assertion methods that are only available for those subject types.

Without the `isA<T>()` assertion the code would not compile:

```kotlin
val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
expect(subject.get("count")).isGreaterThan(0) // isGreaterThan does not exist on Assertion<Any>
expect(subject.get("name")).hasLength(3) // hasLength does not exist on Assertion<Any>
```

This mechanism means that IDE code-completion is optimally helpful as only assertion methods that are appropriate to the subject type will be suggested. 

## Assertions on elements of a collection

Some assertions on collections include sub-assertions applied to the elements of the collection.
For example, we can assert that _all_ elements conform to a repeated assertion.

```kotlin
val subject = setOf("catflap", "rubberplant", "marzipan")
expect(subject).all {
  isLowerCase()
  startsWith('c')
}
```

Produces the output:

```
✘ [catflap, rubberplant, marzipan] all elements match predicate: 
  ✔ "catflap" starts with 'c'
  ✔ "catflap" is lower case
  ✘ "rubberplant" starts with 'c'
  ✔ "rubberplant" is lower case
  ✘ "marzipan" starts with 'c'
  ✔ "marzipan" is lower case
```

The results are broken down by individual elements in the collection so it's easy to see which failed.

## Asserting exceptions are thrown

To assert that some code throws an exception you can use the `throws<E>` function.
For example:

```kotlin
throws<TooMuchFlaxException> {
  service.computeMeaning()
}
```

The `throws<E>` function returns an `Assertion<E>` so you can chain assertions about the exception after it.

## Mapping assertions using lambdas

In order to map the assertion chain to a property or method result of the current subject you can use the `map` method.
The method takes a lambda whose receiver is the current subject and returns an `Assertion<R>` where `R` is whatever the lambda returns.

This is sometimes useful for making assertions about the properties of an object or the values returned by methods.

```kotlin
val subject = Person("David", LocalDate.of(1947, 1, 8))
expect(subject) {
  map { name }.isEqualTo("David")
  map { birthDate }.map { year }.isEqualTo(1947)
}
```

## Writing your own assertion functions

One of the aims of Kirk is that implementing your own assertions is _really, really_ easy.
Assertion functions are [extension functions](https://kotlinlang.org/docs/reference/extensions.html) on the interface `Assertion<T>`.

Simple assertions produce a single message on failure.
They call `assert` passing a lambda with the assertion logic that calls `pass()` or `fail()`.

The standard assertions `isNull`, `isEqualTo`, `isA<T>` and many others are simple assertions implemented just like this.

### Simple assertions

Let's imagine we're implementing an assertion function for `java.time.LocalDate` that tests if the represented date is a leap day.

```kotlin
fun Assertion<LocalDate>.isStTibsDay(): Assertion<LocalDate> =
  assert("is St. Tib's Day") { 
    when (MonthDay.from(subject)) {
      MonthDay.of(2, 29) -> pass()
      else               -> fail()
    }
  }
```
Breaking this down: 

1. We declare the assertion function applies only to `Assertion<LocalDate>`.
2. Note that the function also returns `Assertion<LocalDate>` so we can include this assertion as part of a chain.
3. We call `assert` passing a lambda with the assertion logic.
4. If `subject` is the value we want we call `pass()` otherwise we call `fail()`

If this assertion fails it will produce a message like:

```
✘ 2018-05-01 is St. Tib's Day 
```

#### Where do `subject`, `pass()` and `fail()` come from?

The method `assert` accepts a description for the assertion being made and a lambda function `AssertionContext<T>.() -> Unit`.
That `AssertionContext<T>` receiver provides the lambda everything it needs to access the `subject` of the assertion and report the result via the `pass()` or `fail()` method.

`AssertionContext<T>` also provides the functions `expect(E)` and `expect(E, Assertion<E>.()->Unit)` for building complex nested assertions.

### Nested assertions

For more complex assertion implementations you can "nest" sub-assertions inside your overall assertion.
Nested assertions results are reported under the overall result which can be very useful for providing detailed diagnostic information in case of a failure.

Nested assertions are useful for things like:

- applying assertions to multiple properties of an object, for example for a field-by-field comparison.
- applying assertions to all elements of a collection or entries in a map, reporting on individual elements.

Nested assertions are implemented by using `AssertionContext<T>.expect` to perform assertions inside of the lambda passed to `assert` and then using the properties `allFailed`, `anyFailed`, `allPassed` and `anyPassed` to make a decision about the overall result.
For example, `all` applies assertions to each element of an `Iterable` then passes the overall assertion if (and only if) all those nested assertions passed.
On the other hand `any` applies assertions to the elements of an `Iterable` but will pass the overall assertion if just one of those nested assertions passed. 
