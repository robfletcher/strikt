title=Strongly Typed Assertions
type=page
status=published
cached=true
previousPage=expecting-exceptions.html
nextPage=mapping.html
~~~~~~

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
val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
expectThat(subject.get("count"))
  .isA<Number>()
  .isGreaterThan(0)

expectThat(subject.get("name"))
  .isA<String>()
  .hasLength(3)
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

## Grouping assertions after a null or type check

It's frequently useful to be able to perform a block of assertions after narrowing the subject type.
For example, if the declared type of an assertion subject is nullable it can be awkward to apply a block of assertions directly with `expectThat` as every individual assertion in the block needs to deal with the nullable type.
The same is true when the subject type is overly broad and you need to narrow the type with `isA<T>` in order to use assertion functions that are specific to the runtime type.

To handle this scenario Strikt provides the `and` method that is used to add a block of assertions to a chain.
For example:

```kotlin
expectThat(subject)  
  .isNotNull()   
  .and {
    // perform other assertions on a known non-null subject 
  }
```

The type after `expectThat` is `Assertion.Builder<T?>` (assuming `subject` has a nullable declared type) but the receiever of `and` is `Assertion.Builder<T>` as `isNotNull` has narrowed the subject type.

