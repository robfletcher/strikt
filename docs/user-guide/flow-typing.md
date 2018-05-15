# Flow typing

Strikt's API is designed to work with Kotlin's strong type system. 

Chained assertions return an `Assertion<T>` object with a generic type representing the (declared) type of the assertion subject.
Some assertions will return a _different_ type to the one they were called on.

## Nullable subjects

For example, if the subject of an assertion is a nullable type (in other words it's an `Assertion<T?>`) the assertion methods `isNull()` and `isNotNull()` are available.
The return type of `isNotNull()` is `Assertion<T>` because we now _know_ the subject is not null.
You will find IDE code-completion will no longer offer the `isNull()` and `isNotNull()` assertion methods.

## Narrowing assertions

Another example comes when testing values with broad types and making assertions about their specific runtime type.
This is called "narrowing".

For example:

```kotlin
val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
expect(subject.get("count")).isA<Number>().isGreaterThan(0)
expect(subject.get("name")).isA<String>().hasLength(3)
```

The return type of the subject map's `get()` method is `Any` but using the narrowing assertion `isA<T>()` we can both assert the type of the value and -- because the compiler now knows it is dealing with an `Assertion<String>` or an `Assertion<Number>` -- we can use more specialized assertion methods that are only available for those subject types.

Without the `isA<T>()` assertion the code would not compile:

```kotlin
val subject: Map<String, Any> = mapOf("count" to 1, "name" to "Rob")
expect(subject.get("count")).isGreaterThan(0) // isGreaterThan does not exist on Assertion<Any>
expect(subject.get("name")).hasLength(3) // hasLength does not exist on Assertion<Any>
```

This mechanism means that IDE code-completion is optimally helpful as only assertion methods that are appropriate to the subject type will be suggested. 

## Mapping assertions

In order to map the assertion chain to a property or method result of the current subject you can use the `map` method.

### Mapping with lambdas

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

If you use a Kotlin property or Java method reference as the lambda passed to `map`, Strikt will automatically derive the property name and use it as the subject description on the returned assertion. 
This is very useful for generating good quality assertion output with minimal effort.

For example, if the previous example fails it will format the error message like this:

```
▼ Person[name: Ziggy, birthDate: 1972-06-16] 
  ▼ Ziggy 
    ✗ is equal to David
  ▼ 1972-06-16 
    ▼ 1972
      ✗ is equal to 1947
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
  ▼ .name Ziggy 
    ✗ is equal to David
  ▼ .birthDate 1972-06-16
    ▼ .year 1972 
      ✗ is equal to 1947
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
