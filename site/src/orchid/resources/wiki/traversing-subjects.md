---
---

# Traversing Assertion Subjects

Although you can obviously write assertions for the properties of an object with code like this:

```kotlin
{% snippet 'traversing_subjects_1'%}
```

Sometimes it's useful to be able to transform an assertion on a subject to an assertion on a property of that subject, or the result of a method call.
Particularly when using soft assertion blocks.

Strikt allows for this using the `Assertion.Builder<T>.get` method.

## Using _get_ with property or method references

The first override of `get` takes a property or (zero argument) method reference as a parameter.
The `get` method returns an `Assertion.Builder<R>` where the new subject (whose type is `R`) is the value returned by invoking that property or method on the current subject.

This is useful for making assertions about the properties of an object, or the values returned by methods, particularly if you want to use a block-style assertion to validate multiple object properties.

```kotlin
{% snippet'traversing_subjects_4' %}
```

## Using _get_ with lambdas

An alternate version of the `get` method takes a lambda whose receiver is the current subject.

```kotlin
{% snippet 'traversing_subjects_2' %}
```

Strikt will attempt to read the test source to find out the name of the variables.
This example produces output that looks like this:

```kotlin
{% snippet 'traversing_subjects_3' %}
```

### Performance considerations

Reading the test source can be costly performance-wise.
If you are running large-scale parallel tests, property-based testing, or something similar, it probably makes sense to avoid this penalty.
You can do so by:

* providing an explicit description parameter to `get` in addition to the lambda.
* using `get` with a property/method reference rather than a lambda.

In either of those cases Strikt will _not_ derive a description by attempting to read the source.

## Mapping elements of collections

If the assertion subject is an `Iterable` Strikt provides a `map` function much like the one in the Kotlin standard library.
It is effectively like using `get` on each element of the `Iterable` subject.

```kotlin
{% snippet 'traversing_subjects_5' %}
```

In this case the `map` function is transforming the `Assertion.Buidler<List<Person>>` into an `Assertion.Builder<List<String>>` by applying the `name` property to each element.

## Re-usable mapping extensions

If you find yourself frequently using `get` for the same properties or methods, consider defining extension property or method to make things even easier.

For example:

```kotlin
{% snippet 'traversing_subjects_6' %}
```

You can then write the earlier example as:

```kotlin
{% snippet 'traversing_subjects_7' %}
```

## Built-in traversals

Strikt has a number of built in traversal properties and functions such as `Assertion.Builder<List<E>>.first()` which returns an `Assertion.Builder<E>` whose subject is the first element of the list.
See the {{ anchor('API docs', 'strikt.assertions') }} for details.
