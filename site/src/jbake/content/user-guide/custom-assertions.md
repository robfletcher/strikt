title=Custom Assertions
type=page
status=published
cached=true
previousPage=mapping.html
nextPage=additional-modules.html
~~~~~~

# Custom Assertions

One of the aims of Strikt is that implementing your own assertions is _really, really_ easy.
Assertion functions are [extension functions](https://kotlinlang.org/docs/reference/extensions.html) on the interface `Assertion.Builder<T>` where `T` is the type of the assertion subject.

### Tip: Avoiding clashing method signatures

Because of Java's generic type erasure, it's possible that the name of your custom assertion function may clash with an existing one for a different subject type.

To avoid this you can add the `@JvmName` annotation to your assertion function to disambiguate the methods.
See [Handling signature clashes with `@JvmName`](https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#handling-signature-clashes-with-jvmname) in the Kotlin documentation.

For example, if you were defining an `isEmpty` assertion for Gson's `JsonArray` type, you would need to disambiguate it from the `isEmpty` assertion Strikt provides for collections.

```kotlin
@JvmName("isEmpty_JsonArray")
fun Assertion.Builder<JsonArray>.isEmpty(): Assertion.Builder<JsonArray> =
  // ...
```

## Atomic assertions

"Atomic" assertions produce a single message on failure.
They call `assert` passing a lambda with the assertion logic that calls `pass()` or `fail()`.

The standard assertions `isNull`, `isEqualTo`, `isA<T>` and many others are simple assertions implemented just like this.

Let's imagine we're implementing an assertion function for `java.time.LocalDate` that tests if the represented date is a leap day.

```kotlin
fun Assertion.Builder<LocalDate>.isStTibsDay(): Assertion.Builder<LocalDate> =
  assert("is St. Tib's Day") { 
    when (MonthDay.from(it)) {
      MonthDay.of(2, 29) -> pass()
      else               -> fail()
    }
  }
```

Breaking this down: 

1. We declare the assertion function applies only to `Assertion.Builder<LocalDate>`.
2. Note that the function also returns `Assertion.Builder<LocalDate>` so we can include this assertion as part of a chain.
3. We call `assert` passing a description of the assertion and a lambda with the assertion logic.
4. If `it` (the test subject) is the value we want we call `pass()` otherwise we call `fail()`

If this assertion fails it will produce a message like:

```
▼ Expect that 2018-05-01:
  ✗ is St. Tib's Day 
```

### Note

The method `assert` accepts a description for the assertion being made and a lambda function `Assertion<T>.(T) -> Unit`.
The parameter passed to the lambda is the assertion subject.
The `Assertion<T>` receiver provides the lambda the `pass()` and `fail()` methods for reporting the assertion result.

## Describing the "actual" value

For assertions that perform a comparison between actual and expected values it is usually helpful to describe the value that was incorrect.
That way any assertion failure message will be more helpful.

In order to do this, Strikt provides an overridden version of `fail()` that accepts a message string and the actual value found.
The message string should contain a format placeholder for the value.

```kotlin
fun Assertion.Builder<LocalDate>.isStTibsDay(): Assertion.Builder<LocalDate> =
  assert("is St. Tib's Day") { 
    when (MonthDay.from(it)) {
      MonthDay.of(2, 29) -> pass()
      else               -> fail(
        message = "in fact it is %s", 
        actual = subject
      )
    }
  }
```

Now if the assertion fails there is a little more detail.

```
▼ Expect that 2018-05-01:
  ✗ is St. Tib's Day : in fact it is 2018-05-01
```

In this case that's not terribly helpful but when dealing with properties, method return values, or the like it can save a lot of effort in identifying the precise cause of an error.
When comparing large string values it also means IDEs such as IntelliJ IDEA will present a detailed diff dialog in the event of a test failure.

## Simple atomic assertions with boolean expressions

For the simplest assertion functions, instead of using `assert` and calling `pass` or `fail`, you can use `passesIf` with a lambda whose parameter is the assertion subject that returns a boolean.

We can re-implement the example above like this:

```kotlin
fun Assertion.Builder<LocalDate>.isStTibsDay(): Assertion.Builder<LocalDate> =
  passesIf("is St. Tib's Day") { 
    MonthDay.from(it) == MonthDay.of(2, 29)
  }
```

You should not use this form when you want to provide a meaningful description of the actual value but for simple assertions it's slightly less verbose.

## Composed assertions

For more complex assertion implementations you can "nest" sub-assertions inside your overall assertion.
Composed assertions' results are reported under the overall result which is useful for providing detailed diagnostic information in case of a failure.

Composed assertions are useful for things like:

- applying assertions to multiple properties of an object, for example for a field-by-field comparison.
- applying assertions to all elements of a collection or entries in a map, reporting on individual elements.

Imagine we're creating an assertion function that tests fails if any element of a collection is `null`.

```kotlin
fun <T: Iterable<E?>, E> Assertion.Builder<T>.containsNoNullElements(): Assertion.Builder<T> =
  compose("does not contain any null elements") { subject ->
    subject.forEach {
      expect(it).isNotNull()
    }
  } then {
    if (allPassed) pass() else fail()
  }
```

Breaking this down:

1. We declare the overall assertion function applies to an `Iterable` of a nullable element type `E`.
2. We use the `compose` method instead of `assert`.
3. Inside the `compose` block we make assertions about each element of the iterable subject.
4. Inside the `then` block we pass or fail the overall assertion depending on whether the nested assertions all passed.

The receiver of the block passed to `result` has the properties `allFailed`, `anyFailed`, `allPassed` and `anyPassed` along with `pass()` and `fail()` functions used in simple assertions.

If the assertion failed we'll see something like this:

```
▼ Expect that ["catflap", null, "rubberplant", "marzipan"]: 
  ✗ does not contain any null elements
    ▼ "catflap": 
      ✓ is not null  
    ▼ null: 
      ✗ is not null  
    ▼ "rubberplant": 
      ✓ is not null  
    ▼ "marzipan": 
      ✓ is not null  
```

As well as the overall assertion failure message we get a detailed breakdown allowing us to easily find exactly where the problem is.

Several assertion functions in Strikt's standard assertions library use nested assertions.
For example, `Assertion.Builder<Iterable<E>>.all` applies assertions to each element of an `Iterable` then passes the overall assertion if (and only if) all those nested assertions passed (`allPassed`).
On the other hand `Assertion.Builder<Iterable<E>>.any` applies assertions to the elements of an `Iterable` but will pass the overall assertion if at least one of those nested assertions passed (`anyPassed`).
The `Assertion.Builder<Iterable<E>>.none` assertion passes only if `allFailed` is true for its nested assertions! 
