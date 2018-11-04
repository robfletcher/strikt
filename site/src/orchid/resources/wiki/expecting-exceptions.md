---
---

# Asserting exceptions are thrown

To assert that some code throws an exception you can use the `catching` function that accepts a lambda `() -> Unit` that performs the operation that should throw an exception and the `throws<E>` assertion function.
For example:

{% codesnippet key='catching_exceptions_1' testClass='Assertions' %}

The `catching` function returns `Throwable?` with the value being whatever exception is thrown by the lambda, or `null` if nothing is thrown.
Combining it with the `throws<E>` assertion allows testing for specific exception types.
The `throws<E>` assertion will fail if the exception is `null` or the wrong type.

The `throws<E>` function returns an `Assertion.Builder<E>` so you can chain assertions about the exception itself after it.

If you just need to test that _any_ exception was or was not thrown you can combine `catching` with `isNull` or `isNotNull`.
For example:

{% codesnippet key='catching_exceptions_2' testClass='Assertions' %}

## Shorthand form

You can also use the `expectThrows<E>(A)` function which is simply a shorthand for the `expectThat` / `catching` / `throws` combination.
For example:

{% codesnippet key='expect_throws_1' testClass='Assertions' %}

