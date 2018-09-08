title=Expecting Exceptions
type=page
status=published
cached=true
previousPage=collection-elements.html
nextPage=flow-typing.html
~~~~~~

# Asserting exceptions are thrown

To assert that some code throws an exception you can use an assertion on a lambda `() -> Unit` that performs the operation that should throw an exception and the `throws<E>` assertion function.
For example:

```kotlin
expectThat { service.computeMeaning() }
  .throws<TooMuchFlaxException>()
```

The `throws<E>` function returns an `Assertion.Builder<E>` so you can chain assertions about the exception after it.

There is also a top level function `expectThrows(() -> Unit)` that makes this even more concise.

```kotlin
expectThrows<TooMuchFlaxException> { 
  service.computeMeaning() 
}
```
