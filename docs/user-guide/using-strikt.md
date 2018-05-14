# Using Strikt

Strikt does not depend on any particular test runner.
It can be used with JUnit, Spek or any other runner that supports tests written in Kotlin.

## Assertion styles

Two different styles of assertion -- chained and block -- are supported for different use-cases.
You can mix and match both in the same test and even nest chained assertions inside block assertions.

### Chained assertions

Chained assertions use a fluent API.
They fail fast.
That is, the first assertion that fails breaks the chain and further assertions are not evaluated.

Each assertion in the chain returns an `Assertion` object that supports further assertions.

```kotlin
val subject = "covfefe"
expect(subject)
  .isA<String>()
  .hasLength(1)
  .isUpperCase()
```

Produces the output: 

```
✔ "covfefe" is a java.lang.String
✘ "covfefe" has length 1
```

Notice that the `isUpperCase()` assertion is not applied as the earlier `hasLength(1)` assertion failed.

### Block assertions

Block assertions are declared in a block whose receiver is an assertion on a target object.
Block assertions do _not_ fail fast.
That is, all assertions in the block are evaluated and the result of the "compound" assertion will include results for all the assertions made in the block.

```kotlin
val subject = "covfefe"
expect(subject) {
  isA<String>()
  hasLength(1)
  isUpperCase()
}
```

Produces the output:

```
✔ "covfefe" is a java.lang.String
✘ "covfefe" has length 1
✘ "covfefe" is upper case
```

All assertions are applied and since two fail there are two errors logged.

### Chained assertions inside block assertions

Chained assertions inside a block _will_ still fail fast but will not prevent other assertions in the block from being evaluated.

```kotlin
val subject = 1L
expect(subject) {
  lessThan(1).isA<Int>()
  greaterThan(1)
}
```

Produces the output:

```
✘ 1 is less than 1
✘ 1 is greater than 1
```

Note the `isA<Int>` assertion (that would have failed) was not evaluated since it was chained after `lessThan(1)` which failed.
The `greaterThan(1)` assertion _was_ evaluated since it was not part of the same chain.

## Flow typed assertions

Chained assertions return an `Assertion<T>` object with a generic type representing the (declared) type of the assertion subject.
Some assertions will return a _different_ type to the one they were called on.
For example, if the subject of an assertion is a nullable type (in other words it's an `Assertion<T?>`) the assertion methods `isNull()` and `isNotNull()` are available.
The return type of `isNotNull()` is `Assertion<T>` because we now _know_ the subject is not null.
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
expect(subject).all {
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

## Asserting exceptions are thrown

To assert that some code throws an exception you can use the `throws<E>` function.
For example:

```kotlin
throws<TooMuchFlaxException> {
  service.computeMeaning()
}
```

The `throws<E>` function returns an `Assertion<E>` so you can chain assertions about the exception after it.

## Mapping assertions using lambdas

In order to map the assertion chain to a property or method result of the current subject you can use the `map` method.
The method takes a lambda whose receiver is the current subject and returns an `Assertion<R>` where `R` is whatever the lambda returns.

This is sometimes useful for making assertions about the properties of an object or the values returned by methods.

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expect(subject) {
  map { name }.isEqualTo("David")
  map { birthDate }.map { year }.isEqualTo(1947)
}
```

### Mapping with property or getter references

If you use a Kotlin property or Java getter reference as the lambda passed to `map`, Strikt will automatically derive the property name and use it as the subject description on the returned assertion. 
This is very useful for generating good quality assertion output with minimal effort.

For example, if the previous example fails it will format the error message like this:

```
▼ Person[name: Ziggy, birthDate: 1972-06-16] 
  ✘ Ziggy is equal to David
  ✘ 1972 is equal to 1947
```

However, using property references, the output is more useful.

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expect(subject) {
  map(Person::name).isEqualTo("David")
  map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
}
```

```
▼ Person[name: Ziggy, birthDate: 1972-06-16] 
  ✘ .name Ziggy is equal to David
  ✘ .birthDate.year 1972 is equal to 1947
```

### Mapping with extension properties

Perhaps the most useful application of `map` is in defining extension properties that map an assertion on a type to an assertion on one of the properties (or method return values) of that type.

A simple example is the standard extension property Strikt supplies for `Assertion<Collection<*>>` that maps to an assertion on the collection's `size`.

```kotlin
val <T : Collection<*>> Assertion<T>.size: Assertion<Int>
  get() = map(Collection<*>::size)
```

It is very easy to define these kind of extension properties for testing your own types.

For example:

```kotlin
val Assertion<Person>.name: Assertion<String>
  get() = map(Person::name)

val Assertion<Person>.dateOfBirth: Assertion<LocalDate>
  get() = map(Person::dateOfBirth)
  
val Assertion<LocalDate>.year: Assertion<Int>
  get() = map(LocalDate::getYear)

```

You can then write the earlier example as:

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expect(subject) {
  name.isEqualTo("David")
  birthDate.year.isEqualTo(1947)
}
```
