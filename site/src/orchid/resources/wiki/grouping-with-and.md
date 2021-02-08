---
---

# Grouping Assertions with _and_ or _with_

Strikt provides the `and` and `with` functions, and several varieties of `with*`, that are used to add a block of assertions to a chain.
This is useful in a couple of scenarios.

## Grouping assertions after a null or type check

It's frequently useful to be able to perform a block of assertions after narrowing the subject type.
For example, if the declared type of an assertion subject is nullable it can be awkward to apply a block of assertions directly with `expectThat` as every individual assertion in the block needs to deal with the nullable type.

The same is true when the subject type is overly broad, and you need to narrow the type with `isA<T>` in order to use assertion functions that are specific to the runtime type.

The `and` method is helpful in these scenarios.
For example:

```kotlin
{% snippet 'grouping_with_and_1' %}
```

The type after `expectThat` is `Assertion.Builder<T?>` (assuming `subject` has a nullable declared type) but the receiver of `and` is `Assertion.Builder<T>` as `isNotNull` has narrowed the subject type.

## Making assertions on sub-trees of a subject

Another use for `and` is to create a branch of assertions that apply to a sub-tree of the subject.
For example, if testing a complex value type with nested properties:

```kotlin
{% snippet 'grouping_with_and_2' %}
```

The `with` function gives you another option for doing this:

```kotlin
{% snippet 'grouping_with_with_1' %}
```

Of course, it may be better to structure the same assertion with separate assertions.
This is a lot more readable:

```kotlin
{% snippet 'grouping_with_and_3' %}
```

Testing properties of a collection can be done similarly:

```kotlin
{% snippet 'grouping_with_and_4' %}
```

## _with*_ extension functions

Strikt provides some variants of `with` that are also useful in these kinds of tests.
These include:

* for `Iterable` subjects:
  * `withElementAt(index, lambda)`
  * `withFirst(lambda)`
  * `withLast(lambda)`
  * `withFirst(predicate, lambda)`
* for `Map` subjects:
  * `withValue(key, lambda)`
* for `CapturingSlot` subjects:
  * `withCaptured(lamda)`

For example, the previous assertions could also be written as:

```kotlin
{% snippet 'grouping_with_with_2' %}
```
