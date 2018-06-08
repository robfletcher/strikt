title=Strikt
type=home
status=published
cached=true
~~~~~~
  
Strikt is an assertion library for Kotlin intended for use with a test runner such as [JUnit](https://junit.org/junit5/) or [Spek](http://spekframework.org/).
It's inspired by [AssertJ](https://joel-costigliola.github.io/assertj/), [Atrium](https://robstoll.github.io/atrium/) and [Hamkrest](https://github.com/npryce/hamkrest).
However, none of those provided exactly what I wanted so I decided to create my own.

Strikt gets you…



## Detailed reporting

```
▼ Expect that [Eris, Thor]
  ✓ at least one element matches:
    ▼ Expect that Eris
      ▼ .culture "Grœco-Californian"
        ✓ is equal to "Grœco-Californian"
      ▼ .realm "discord and confusion"
        ✓ is equal to "discord and confusion"
      ▼ .aliases ["Ἔρις", "Discordia"]
        ✓ contains the elements ["Discordia"]
          ▼ Expect that ["Ἔρις", "Discordia"]
            ✓ contains "Discordia"
    ▼ Expect that Thor
      ▼ .culture "Norse"
        ✗ is equal to "Grœco-Californian"
      ▼ .realm "thunder"
        ✗ is equal to "discord and confusion"
      ▼ .aliases ["Þórr", "Þunor"]
        ✗ contains the elements ["Discordia"]
          ▼ Expect that ["Þórr", "Þunor"]
            ✗ contains "Discordia"
```
