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
 