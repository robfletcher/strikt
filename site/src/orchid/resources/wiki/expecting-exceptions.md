---
---

# Expecting exceptions

To assert that some code does or does not throw an exception use the `expectCatching` function that accepts a lambda `() -> Any?` that performs the operation that may throw an exception and the `succeeded()` or `failed()` assertion functions.
For example:

{% codesnippet key='catching_exceptions_1' testClass='Assertions' %}

The `expectCatching` function returns `Assertion.Builder<Try<T>>` with the assertion's subject being a wrapper for either the value the lambda returns or the exception it throws.

## Asserting failure

The `failed()` assertion function returns an `Assertion.Builder<Throwable>` so you can chain assertions about the exception itself after it.
For example, combining it with the `isA<T>()` assertion allows testing for specific exception types.

The `failed()` assertion will fail if the lambda does not throw an exception.

If you just need to test that _any_ exception was thrown you can just use the `failed()` assertion by itself.
For example:

{% codesnippet key='catching_exceptions_2' testClass='Assertions' %}

### Shorthand form

You can also use the `expectThrows<E>(A)` function which is simply a shorthand for the `expectCatching` / `failed` / `isA<E>` combination.
For example:

{% codesnippet key='expect_throws_1' testClass='Assertions' %}

## Asserting success

You can also assert that an exception is _not_ thrown by the `expectCatching` lambda using the `succeeded()` assertion function.

The `succeeded()` function returns an `Assertion.Builder<T>` where the type of the chained assertion subject is inferred from the value the lambda returns.
This allows you to chain further assertions about the returned value.
