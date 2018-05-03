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

## Assertion styles

Two different styles of assertion are supported for different use-cases.

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

### Grouped assertions

Grouped assertions are declared in a block whose receiver is an assertion on a target object.
Grouped assertions do _not_ fail fast.
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
The `greaterThan(1)` assertion _was_ evaluated since it was not part of the chain.

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

Assertions come in two basic flavors; atomic and nested.
Atomic assertions produce a single message on failure.
`isNull`, `isEqualTo`, `isA<T>` and so on are all examples of atomic assertions.
Nested assertions check several things about the subject and produce a group of messages that are _nested_ under the main message on failure.
Examples of nested assertions would be those that apply to every element of a collection, such as `all`, or assertions that do field-by-field object comparisons.  

### Atomic assertions

Let's imagine we're implementing an assertion function for `java.time.LocalDate` that tests if the represented date is a leap day.

```kotlin
fun Assertion<LocalDate>.isStTibsDay(): Assertion<LocalDate> =
  atomic("is St. Tib's Day") { 
    when (MonthDay.from(subject)) {
      MonthDay.of(2, 29) -> pass()
      else               -> fail()
    }
  }
```
Breaking this down: 

1. We declare the assertion function applies only to `Assertion<LocalDate>`.
2. Note that the function also returns `Assertion<LocalDate>` so we can include this assertion as part of a chain.
3. We use an `atomic` assertion as we're just applying a single check.
4. If `subject` is the value we want we call `pass()` otherwise we call `fail()`

If this assertion fails it will produce a message like:

```
✘ 2018-05-01 is St. Tib's Day 
```

#### Where do `subject`, `pass()` and `fail()` come from?

The method `atomic` accepts a description for the assertion being made and a lambda function `AtomicAssertionContext<T>.() -> Unit`.
That `AtomicAssertionContext<T>` receiver provides the lambda everything it needs to access the `subject` of the assertion and report the result via the `pass()` or `fail()` method.

### Nested assertions

Nested assertions are implemented in a very similar way to atomic assertions.
The only differences are that you use the `nested` method instead of `atomic` and the receiver is a `NestedAssertionContext<T>` which has a few extra capabilities.

`NestedAssertionContext<T>` has the following properties and methods:

- `subject`, `pass()` and `fail()` are the same as in `AtomicAssertionContext<T>`.
- `expect(E)` and `expect(E, Assertion<E>.() -> Unit)` let you make nested assertions using either chains or blocks.
- `allFailed`, `anyFailed`, `allPassed` and `anyPassed` are boolean properties that report on the outcome of any nested assertions.

A nested assertion will use several `expect` chains or blocks to make assertions then make a decision about whether the _overall_ assertion has passed or failed based on the outcome of those nested assertions.
For example, `all` applies assertions to each element of an `Iterable` then passes the overall assertion if (and only if) all those nested assertions passed.
On the other hand `any` applies assertions to the elements of an `Iterable` but will pass the overall assertion if just one of those nested assertions passed. 

Nested assertions can also be very useful for custom assertions that apply to the domain objects of your own applications.
