---
---

# Traversing Assertion Subjects

Although you can obviously write assertions for the properties of an object with code like this:

{% codesnippet key='traversing_subjects_1' testClass='Chaining' %}

Sometimes it's useful to be able to transform an assertion on a subject to an assertion on a property of that subject, or the result of a method call.
Particularly when using soft assertion blocks.

Strikt allows for this using the `Assertion.Builder<T>.get` method.  

## Using _get_ with lambdas

The method takes a lambda whose receiver is the current subject and returns an `Assertion.Builder<R>` where `R` (the new subject) is the type of whatever the lambda returns.

This is useful for making assertions about the properties of an object or the values returned by methods, particularly if you want to use a block-style assertion to validate multiple object properties.

{% codesnippet key='traversing_subjects_2' testClass='Chaining' %}

Strikt will read the test source to find out the name of the variables.
This example produces output that looks like this:

{% codesnippet key='traversing_subjects_3' testClass='Chaining' %}

## Using _get_ with property or method references

It's also possible to use a property or method reference in place of a lambda. 

{% codesnippet key='traversing_subjects_4' testClass='Chaining' %}

## Mapping elements of collections

If the assertion subject is an `Iterable` Strikt provides a `map` function much like the one in the Kotlin standard library.
It is effectively like using `get` on each element of the `Iterable` subject.

{% codesnippet key='traversing_subjects_5' testClass='Chaining' %}

In this case the `map` function is transforming the `Assertion.Buidler<List<Person>>` into an `Assertion.Builder<List<String>>` by applying the `name` property to each element.

## Re-usable mapping extensions

If you find yourself frequently using `get` for the same properties or methods, consider defining extension property or method to make things even easier.

For example:

{% codesnippet key='traversing_subjects_6' testClass='Chaining' %}

You can then write the earlier example as:

{% codesnippet key='traversing_subjects_7' testClass='Chaining' %}

## Built-in traversals

Strikt has a number of built in traversal properties and functions such as `Assertion.Builder<List<E>>.first()` which returns an `Assertion.Builder<E>` whose subject is the first element of the list.
See the {{ anchor('API docs', 'strikt.assertions') }} for details.
