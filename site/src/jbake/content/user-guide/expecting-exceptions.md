title=Expecting Exceptions
type=page
status=published
cached=true
previousPage=collection-elements.html
nextPage=flow-typing.html
~~~~~~

# Asserting exceptions are thrown

To assert that some code throws an exception you can use the `catching` function that accepts a lambda `() -> Unit` that performs the operation that should throw an exception and the `throws<E>` assertion function.
For example:

```kotlin
expectThat(catching { service.identifyHotdog() })
  .throws<NotHotdogException>()
```

The `catching` function simply returns `Throwable?` with the value being whatever exception is thrown, or `null` if nothing is thrown.
Combining it with the `throws<E>` assertion allows testing for specific exception types.
The `throws<E>` assertion will fail if the exception is `null` or the wrong type.

The `throws<E>` function returns an `Assertion.Builder<E>` so you can chain assertions about the exception after it.

If you just need to test that _any_ exception was or was not thrown you can combine `catching` with `isNull` or `isNotNull`.
For example:

```kotlin
expectThat(catching { service.identifyHotdog() })
  .isNull()
```
