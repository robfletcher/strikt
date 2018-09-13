title=Mapping Over Assertion Subjects
type=page
status=published
cached=true
previousPage=flow-typing.html
nextPage=grouping-with-and.html
~~~~~~

# Mapping Over Assertion Subjects

Although you can obviously write assertions for the properties of an object with code like this:

```kotlin
expectThat(map.size).isEqualTo(1)
expectThat(list.first()).isEqualTo("fnord")
expectThat(person.name).isEqualTo("Ziggy")
```

Sometimes it's useful to be able to transform an assertion on a subject to an assertion on a property of that subject, or the result of a method call.
Particularly when using soft assertion blocks.

Strikt allows for this using the `Assertion.Builder<T>.map` method.  

## Mapping with lambdas

The method takes a lambda whose parameter is the current subject and returns an `Assertion.Builder<R>` where `R` is the type of whatever the lambda returns.

This is sometimes useful for making assertions about the properties of an object or the values returned by methods, particularly if you want to use a block-style assertion to validate multiple object properties.

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expectThat(subject) {
  map { it.name }.isEqualTo("David")
  map { it.birthDate.year }.isEqualTo(1947)
}
```

## Mapping with property or method references

If you use a Kotlin property or Java method reference as the lambda passed to `map`, Strikt will automatically derive the property name and use it as the subject description on the returned assertion. 
This is useful for generating good quality assertion output with minimal effort.

For example, if the previous example fails it will format the error message like this:

```
▼ Expect that Person[name: Ziggy, birthDate: 1972-06-16]: 
  ▼ "Ziggy"
    ✗ is equal to "David" : found "Ziggy"
  ▼ 1972
    ✗ is equal to 1947 : found 1972
```

Using property references the output is more useful (although the test code is more verbose).

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expectThat(subject) {
  map(Person::name).isEqualTo("David")
  map(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
}
```

```
▼ Expect that Person[name: Ziggy, birthDate: 1972-06-16]: 
  ▼ .name:
    ✗ is equal to "David" : found "Ziggy"
  ▼ .birthDate: 
    ▼ .year:
      ✗ is equal to 1947 : found 1972
```

## Re-usable mappings

If you find yourself frequently using `map` for the same properties or methods, consider defining extension property or method to make things even easier.

For example:

```kotlin
val Assertion.Builder<Person>.name: Assertion.Builder<String>
  get() = map(Person::name)

val Assertion.Builder<Person>.yearOfBirth: Assertion.Builder<LocalDate>
  get() = map("year of birth") { it.dateOfBirth.year }
```

You can then write the earlier example as:

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expectThat(subject) {
  name.isEqualTo("David")
  yearOfBirth.isEqualTo(1947)
}
```

## Built-in mappings

Strikt has a number of built in mapping properties and functions such as `Assertion.Builder<List<E>>.first()` which returns an `Assertion.Builder<E>` whose subject is the first element of the list.
See the [API docs](/api/strikt-core/strikt.api/-assertion/) for details.
