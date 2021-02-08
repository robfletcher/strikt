---
---

# Assertions on elements of a collection

Some assertions on collections include sub-assertions applied to the elements of the collection.
For example, we can assert that _all_ elements conform to a repeated assertion.

```kotlin
{% snippet 'collections_2' %}
```

This produces the output:

```text
{% snippet 'collections_1' %}
```

The results are broken down by individual elements in the collection, so it's easy to see which failed.

Similarly, `any` asserts that at least one element passes the nested assertions, `one` succeeds if exactly one element passes the nested assertions, and `none` succeeds if all elements of the collection _fail_ the nested assertions.
