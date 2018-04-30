# Kotlin Assertions

(Yes, this needs a better name)

This is an assertion library for Kotlin intended for use with a test runner such as JUnit or Spek.
It's very much inspired by AssertJ, Atrium and Hamcrest.
However, none of those provided exactly what I wanted so I decided to create my own assertions library.

Two different styles of assertion are supported for different use-cases.

## Chained assertions

Chained assertions use a fluent API.
They fail fast.
That is, the first assertion that fails breaks the chain and further assertions are not evaluated.

Each assertion in the chain returns an `Assertion` object that supports further assertions.

```kotlin
val subject = "covfefe"
expect(subject)
  .hasLength(1)
  .isUpperCase()
```

Produces the output: 

```
✘ "covfefe" has length 7
```

Notice that the `isUpperCase()` assertion is not applied as the earlier `hasLength(1)` assertion failed.

## Grouped assertions

Grouped assertions are declared in a block whose receiver is an assertion on a target object.
Grouped assertions do _not_ fail fast.
That is, all assertions in the block are evaluated and the result of the "compound" assertion will include results for all the assertions made in the block.

```kotlin
val subject = "covfefe"
expect(subject) {
  hasLength(1)
  isUpperCase()
}
```

Produces the output:

```
✘ "covfefe" has length 7
✘ "covfefe" is upper case
```

Both assertions are applied and since both fail there are two errors logged.

Chained assertions inside a block _will_ still fail fast but will not prevent other assertions in the block from being evaluated.

## Flow-sensitive assertion types

Chained assertions return an `Assertion<T>` object with a generic type representing the (declared) type of the assertion subject.
Some assertion types will return a different type to the one they were called on.
For example, if the subject of an assertion is a nullable type (in other words it's an `Assertion<T?>`) the assertion methods `isNull()` and `isNotNull()` are available.
The return type of `isNotNull()` is `Assertion<T>` because we now know the subject is not null.
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
expect(subject).allMatch {
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
 