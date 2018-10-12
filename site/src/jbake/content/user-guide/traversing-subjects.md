title=Traversing Assertion Subjects
type=page
status=published
cached=true
previousPage=flow-typing.html
nextPage=grouping-with-and.html
~~~~~~

# Traversing Assertion Subjects

Although you can obviously write assertions for the properties of an object with code like this:

```kotlin
expectThat(map.size).isEqualTo(1)
expectThat(list.first()).isEqualTo("fnord")
expectThat(person.name).isEqualTo("Ziggy")
```

Sometimes it's useful to be able to transform an assertion on a subject to an assertion on a property of that subject, or the result of a method call.
Particularly when using soft assertion blocks.

Strikt allows for this using the `Assertion.Builder<T>.get` method.  

## Using _get_ with lambdas

The method takes a lambda whose receiver is the current subject and returns an `Assertion.Builder<R>` where `R` (the new subject) is the type of whatever the lambda returns.

This is useful for making assertions about the properties of an object or the values returned by methods, particularly if you want to use a block-style assertion to validate multiple object properties.

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expectThat(subject) {
  get { name }.isEqualTo("David")
  get { birthDate.year }.isEqualTo(1947)
}
```

Strikt will read the test source to find out the name of the variables.
This example produces output that looks like this:
```
▼ Expect that Person(David):
  ▼ name:
    ✗ is equal to "Ziggy" : found "David"
  ▼ birthDate.year:
    ✗ is equal to 1971 : found 1947
```

## Using _get_ with property or method references

It's also possible to use a property or method reference in place of a lambda. 

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expectThat(subject) {
  get(Person::name).isEqualTo("David")
  get(Person::birthDate).map(LocalDate::getYear).isEqualTo(1947)
}
```

## Mapping elements of collections

If the assertion subject is an `Iterable` Strikt provides a `map` function much like the one in the Kotlin standard library.
It is effectively like using `get` on each element of the `Iterable` subject.

```kotlin
val subject: List<Person> = // get list from somewhere
expectThat(subject)
  .map(Person::name)
  .containsExactly("David", "Ziggy", "Aladdin", "Jareth")
```

In this case the `map` function is transforming the `Assertion.Buidler<List<Person>>` into an `Assertion.Builder<List<String>>` by applying the `name` property to each element.

## Re-usable mapping extensions

If you find yourself frequently using `get` for the same properties or methods, consider defining extension property or method to make things even easier.

For example:

```kotlin
val Assertion.Builder<Person>.name: Assertion.Builder<String>
  get() = get(Person::name)

val Assertion.Builder<Person>.yearOfBirth: Assertion.Builder<LocalDate>
  get() = get("year of birth") { dateOfBirth.year }
```

You can then write the earlier example as:

```kotlin
val subject = Person(name = "David", birthDate = LocalDate.of(1947, 1, 8))
expectThat(subject) {
  name.isEqualTo("David")
  yearOfBirth.isEqualTo(1947)
}
```

## Built-in traversals

Strikt has a number of built in traversal properties and functions such as `Assertion.Builder<List<E>>.first()` which returns an `Assertion.Builder<E>` whose subject is the first element of the list.
See the [API docs](/api/strikt-core/strikt.api/-assertion/) for details.
