---
---

# Strongly Typed Assertions

Strikt's API is designed to work with Kotlin's strong type system.

Strikt's assertion API uses the interface `Assertion.Builder<T>`, with the generic type `T` representing the (declared) type of the assertion subject.
Assertion functions such as `isEqualTo` are implemented as extension functions on `Assertion.Builder` with an appropriate generic type.

For example `isEqualTo` is an extension function on `Assertion.Builder<Any?>` as it's useful for many types of subject whereas `isEqualToIgnoringCase` is an extension function on `Assertion.Builder<CharSequence>` since it only makes sense to use it on string-like things.

Some assertion functions will return an `Assertion.Builder` with a _different_, more specific, generic type to the one they were called on.

## Nullable subjects

For example, if the subject of an assertion is a nullable type (in other words it's an `Assertion.Builder<T?>`) the assertion methods `isNull()` and `isNotNull()` are available.
The return type of `isNotNull()` is `Assertion.Builder<T>` because we now _know_ the subject is not null.
You will find IDE code-completion will no longer offer the `isNull()` and `isNotNull()` assertion methods.

## Narrowing assertions

Another example is making assertions about a subject's specific runtime type, or "narrowing".

For example:

```kotlin
{% snippet 'flow_typing_1' %}
```

The return type of the subject map's `get()` method is `Any` but using the narrowing assertion `isA<T>()` we can both assert the type of the value and, because the compiler now knows it is dealing with an `Assertion.Builder<String>` or an `Assertion.Builder<Number>`, we can use more specialized assertion methods that are only available for those subject types.

Without the `isA<T>()` assertion the code would not compile:

```kotlin
val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
expectThat(subject.get("count"))
  .isGreaterThan(0)
  // isGreaterThan does not exist on Assertion.Builder<Any>

expectThat(subject.get("name"))
  .hasLength(3)
  // hasLength does not exist on Assertion.Builder<Any>
```

This mechanism means that IDE code-completion is optimally helpful as only assertion methods that are appropriate to the subject type will be suggested.
