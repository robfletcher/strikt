title=Common Assertion Patterns
type=page
status=published
cached=true
previousPage=assertion-styles.html
nextPage=flow-typing.html
~~~~~~

# Common Assertion Patterns

This section contains some common uses of Strikt's standard assertion library.

## Grouping assertions after a null-check

If the declared type of the assertion subject is nullable it can be awkward to use a block of assertions directly with `expect` as every individual assertion in the block needs to deal with the nullable type.
Instead Strikt provides the `and` method that may be used to add a block of assertions to a chain.
For example:

```kotlin
expect(subject)
  .isNotNull()
  .and {
    // perform other assertions on a known non-null subject 
  }
```

The type after `expect` is `Assertion.Builder<T?>` (assuming `subject` has a nullable declared type) but the receiever of `and` is `Assertion.Builder<T>` as `isNotNull` has narrowed the subject type.

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

This produces the output:

```
▼ Expect that [catflap, rubberplant, marzipan]:
  ✗ all elements match:
    ▼ "catflap":
      ✓ starts with 'c'
    ▼ "rubberplant":
      ✗ starts with 'c'
    ▼ "marzipan":
      ✗ starts with 'c'
```

The results are broken down by individual elements in the collection so it's easy to see which failed.

## Asserting exceptions are thrown

To assert that some code throws an exception you can use an assertion on a lambda `() -> Unit` that performs the operation that should throw an exception and the `throws<E>` assertion function.
For example:

```kotlin
expect { service.computeMeaning() }
  .throws<TooMuchFlaxException>()
```

The `throws<E>` function returns an `Assertion.Builder<E>` so you can chain assertions about the exception after it.

There is also a top level function `throws( () -> Unit )` that makes this even more concise.

```kotlin
throws<TooMuchFlaxException> { 
  service.computeMeaning() 
}
```
