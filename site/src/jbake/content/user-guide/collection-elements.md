title=Collection Element Assertions
type=page
status=published
cached=true
previousPage=assertion-styles.html
nextPage=expecting-exceptions.html
~~~~~~

# Assertions on elements of a collection

Some assertions on collections include sub-assertions applied to the elements of the collection.
For example, we can assert that _all_ elements conform to a repeated assertion.

```kotlin
val subject = setOf("catflap", "rubberplant", "marzipan")
expectThat(subject).all {
  isLowerCase()
  startsWith('c')
}
```

This produces the output:

```
▼ Expect that [catflap, rubberplant, marzipan]:
  ✗ all elements match:
    ▼ "catflap":
      ✓ is lower case
      ✓ starts with 'c'
    ▼ "rubberplant":
      ✓ is lower case
      ✗ starts with 'c'
    ▼ "marzipan":
      ✓ is lower case
      ✗ starts with 'c'
```

The results are broken down by individual elements in the collection so it's easy to see which failed.

Similarly, `any` asserts that at least one element passes the nested assertions and `none` succeeds if all elements of the collection _fail_ the nested assertions.
