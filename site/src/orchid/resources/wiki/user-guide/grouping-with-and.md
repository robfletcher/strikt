---
---

# Grouping Assertions with _and_

Strikt provides the `and` method that is used to add a block of assertions to a chain.
This is useful in a couple of scenarios.

## Grouping assertions after a null or type check

It's frequently useful to be able to perform a block of assertions after narrowing the subject type.
For example, if the declared type of an assertion subject is nullable it can be awkward to apply a block of assertions directly with `expectThat` as every individual assertion in the block needs to deal with the nullable type.

The same is true when the subject type is overly broad and you need to narrow the type with `isA<T>` in order to use assertion functions that are specific to the runtime type.

The `and` method is helpful in these scenarios.
For example:

{% codesnippet key='grouping_with_and_1' testClass='Chaining' %}

The type after `expectThat` is `Assertion.Builder<T?>` (assuming `subject` has a nullable declared type) but the receiever of `and` is `Assertion.Builder<T>` as `isNotNull` has narrowed the subject type.

## Making assertions on sub-trees of a subject

Another use for `and` is to create a branch of assertions that apply to a sub-tree of the subject.
For example, if testing a complex value type with nested properties:

{% codesnippet key='grouping_with_and_2' testClass='Chaining' %}

Of course, it may be better to structure the same assertion with separate assertions.
This is a lot more readable:

{% codesnippet key='grouping_with_and_3' testClass='Chaining' %}

Testing properties of a collection can be done in a similar way:

{% codesnippet key='grouping_with_and_4' testClass='Chaining' %}
