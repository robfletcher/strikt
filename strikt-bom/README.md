---
---

## [Using the Bill of Materials (BOM)]({{page.link}})

If you're using multiple Strikt modules (and Gradle >= 5.0) you can import Strikt's BOM and then omit versions for individual modules.
For example:

```kotlin
dependencies {
  testImplementation(platform("io.strikt:strikt-bom:{{ site.version }}"))
  testImplementation("io.strikt:strikt-arrow")
  testImplementation("io.strikt:strikt-jackson")
  testImplementation("io.strikt:strikt-jvm")
}
```
