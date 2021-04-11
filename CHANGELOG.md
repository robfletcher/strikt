## 0.30.1 Ascending Ivory = 2021-04-11

- Fixes an issue where pending assertion chains in a block took precedence over failures, causing false positives. See [#243](https://github.com/robfletcher/strikt/issues/243)

## 0.30.0 Sorrowful Glass - 2021-03-21

- All transitive dependencies are now resolvable from Maven Central rather than needing JCenter.
- Adds `isContainedIn` assertion.

## 0.29.0 Hateful Mantis - 2021-02-06

- Moves several JVM-specific assertions to the new `strikt-jvm` module.
- Removes the `strikt-java-time` module (use `strikt-jvm` instead).
- Adds `isSorted` assertion for `Iterable<Comparable<*>>`.
- Broadens `isSorted(Comparator)` so that it applies to `Iterable<*>` rather than `Collection<*>`.

## 0.28.2 Hidden Ruin - 2021-01-26

- Removes support for Arrow's `Try` type that has been removed from `arrow-core`.
- Adds `anyIndexed`, `allIndexed`, and `noneIndexed` assertions for `Iterable<*>`.

## 0.28.1 Distant Princess - 2020-12-01

- Adds `count()` and `count(predicate)` mappings for `Iterable` subjects.
- Additional support for `File` subjects. See [#230](https://github.com/robfletcher/strikt/pull/230)
  - mapping property `Assertion.Builder<File>.parentFile: Builder<File>`
  - mapping property `Assertion.Builder<File>.lastModified: Long`
  - mapping property `Assertion.Builder<File>.length: Long`
  - mapping property `Assertion.Builder<File>.childFiles: Builder<List<File>>`
  - mapping function `Assertion.Builder<File>.childFile(name): Builder<File>`
  - assertion `Assertion.Builder<File>.exists()`
  - assertion `Assertion.Builder<File>.notExists()`
  - assertion `Assertion.Builder<File>.isRegularFile()`
  - assertion `Assertion.Builder<File>.isNotRegularFile()`
  - assertion `Assertion.Builder<File>.isDirectory()`
  - assertion `Assertion.Builder<File>.isNotDirectory()`
  - assertion `Assertion.Builder<File>.isReadable()`
  - assertion `Assertion.Builder<File>.isNotReadable()`
  - assertion `Assertion.Builder<File>.isWritable()`
  - assertion `Assertion.Builder<File>.isNotWritable()`
  - assertion `Assertion.Builder<File>.isExecutable()`
  - assertion `Assertion.Builder<File>.isNotExecutable()`

## 0.28.0 Invincible Sadness - 2020-09-13

- Fixes compilation using Android due to use of private interface methods. See [#229](https://github.com/robfletcher/strikt/issues/229)
- Assertion chains that use `not` will now report actual values if they fail. See [#222](https://github.com/robfletcher/strikt/issues/222)

## 0.27.0 Hungry Spear - 2020-08-18

Kotlin 1.4 compatibility

## 0.26.1 Quicksilver Silhouette - 2020-05-11

- `Assertion.Builder<Iterable<E>>withFirst`
- `Assertion.Builder<Iterable<E>>withFirst(predicate)`
- `Assertion.Builder<Iterable<E>>withLast`
- `Assertion.Builder<Iterable<E>>withElementAt`
- `Assertion.Builder<Map<K, V>>withValue`
- `Assertion.Builder<CapturingSlot<T>>withCaptured` in _strikt-mockk_.

## 0.26.0 Ferocious Daughter - 2020-05-04

- `succeeded` and `failed` deprecated in favor of `isSuccess` and `isFailure`.
- added `Assertion.Builder<Enum<T>>.isOneOf` assertion.
- `single` now displays the number of elements found if it fails. See [#210](https://github.com/robfletcher/strikt/issues/210)
- Improved description of `task` mapping. See [#218](https://github.com/robfletcher/strikt/issues/218)

## 0.25.0 Blighted Depths - 2020-03-27

- `strikt-mockk` module.

## 0.24.0 Auspicious Misery - 2020-02-25

- New methods:
  - `Assertion.Builder<Iterable<T>>.elementAt`
  - `Assertion.Builder<Iterable<T>>.one`
  - `Assertion.Builder<ArrayNode>.findValuesAsText`
  - `Assertion.Builder<ArrayNode>.textValues`

## 0.23.7 Celestial Depths - 2020-02-13

- Fixes filepeek support on Windows

## 0.23.6 Weeping Spire - 2020-02-10

- Fixes missing transitive dependency on `com.christophsturm:filepeek`

## 0.23.5 Auspicious Misery - 2020-02-09

- Fixes some errors in complex nested block scenarios. See [#203](https://github.com/robfletcher/strikt/issues/203) and [#204](https://github.com/robfletcher/strikt/issues/204)

## 0.23.4 Grievous Form - 2020-01-06

- Fixes BOM file from previous release

## 0.23.3 Savage Wing - 2020-01-01

- Improved descriptions associated with Jackson extensions. See [#195](https://github.com/robfletcher/strikt/issues/195)
- Added `at` mapping for `JsonNode` assertions. See [#152](https://github.com/robfletcher/strikt/issues/152)

## 0.23.2 Mighty Empress - 2019-12-11

- Fixes `IllegalArgumentException` on `compose` when prior assertions have failed because the subject is the wrong type. See [#194](https://github.com/robfletcher/strikt/issues/194)

## 0.23.1 Bounteous Moon - 2019-12-09

Re-release of v0.23.0 following broken release

## 0.23.0 Rancorous Horizon - 2019-12-09

- Adds `with` function for performing blocks of assertions on a derived subject. See [#117](https://github.com/robfletcher/strikt/issues/117)
- Many assertion functions may now be used in infix style. See [#187](https://github.com/robfletcher/strikt/pull/187)
- Improves presentation of multi-line values in expectation output. See [#158](https://github.com/robfletcher/strikt/issues/158)
- The `expectThat` override that accepts a lambda is now `Unit` rather than returning `Assertion.Builder`. See [#190](https://github.com/robfletcher/strikt/issues/190)

## 0.22.3 Vermillion Warrior - 2019-11-23

- Adds assertions for sequence of values being present in a list
- Fixes to documentation in Arrow module

## 0.22.2 Flawless Assassin - 2019-09-25

- Fixes transitive dependency on opentest4j for consumers
- Updates Arrow dependency

## 0.22.1 Blessed Hammer - 2019-09-16

- Fixes documentation link that broke previous release

## 0.22.0 Rancourous Dirge - 2019-09-15

- New module for assertions on Arrow's `Either`, `Try` and `Option` types.

## 0.21.2 Funereal Daughter - 2019-09-14

- Fixes issues with `and` (see #176)
- Kotlin 1.3.50

## 0.21.1 Heavenly Bear - 2019-06-22

- Bug-fix for type inference in `succeeded()`.
- Improved output for `failed()`.

## 0.21.0 Funereal Raven - 2019-06-16

- Updated exception handling assertions that use Kotlin's `runCatching` and `Result<T>`.
- Added override for `describedAs` that accepts a lambda.

## 0.20.1 Elder Refuge - 2019-05-10

- Changed `Assertion.Builder<Iterable<*>>.contains` to pass if the argument is an empty list/varargs.
- Fixed an out of bounds exception in `Assertion.Builder<Iterable<*>>.containsExactly`

## 0.20.0 Pain Mother - 2019-04-15

Added `strikt.gradle` module with some support for testing Gradle's API.

## 0.19.7 Glorious Catacomb - 2019-04-02

- Adds assertions and mappings for `java.io.File` and `java.nio.file.Path`
- Adds assertions for Spring's `ResponseEntity` class.

## 0.19.6 Bitter Forest - 2019-03-30

Fixes issues with bom publication.

## 0.19.5 Victorious Scholar - 2019-03-29

Packages a BOM for aligning strikt versions.

## 0.19.4 Poison Spire - 2019-03-24

Suppresses internal stack frames from exceptions to make it easier to determine where the assertion was raised.

## 0.19.3 Iron Ocean - 2019-03-22

- Adds assertions and mappings for `Assertion.Builder<ClosedRange>`.
- Adds `endsWith(Char)` for `Assertion.Builder<CharSequence>`.

## 0.19.2 Bounteous Steel - 2019-03-13

- Adds `getValue` assertion/mapping for `Assertion.Builder<Map<*, *>>`.

## 0.19.1 Mighty Rainbow - 2019-03-12

- Adds overload of `not` that accepts a block.

## 0.19.0 Poison Hammer - 2019-03-10

- Ensures that chains inside of blocks still fail fast.
- Ensures `get` is runtime type safe. It's now a no-op when preceded by a failing type-narrowing assertion.

## 0.18.2 Venomous Twilight - 2019-03-03

Added `atLeat`, `atMost` and `exactly` assertions for iterables.

## 0.18.1 Ferocious Rose - 2019-03-03

Extends `strikt-jackson` module with further assertion and mapping functions:

- `hasNodeType(JsonNodeType)`
- `isMissing()`
- `textValue()`
- `numberValue()`
- `booleanValue()`

## 0.18.0 Silken Grasshopper - 2019-03-02

Adds `strikt-jackson` module.

## 0.17.3 Unfettered Serpent - 2019-02-24

Adds `isPresent` and `isAbsent` assertions for `java.util.Optional` along with `toNullable` mapping function.

## 0.17.2 Battleworn Understanding - 2019-01-25

Fixes `isSorted` when used in a block.

## 0.17.1 Whispering Sword - 2018-11-30

Adds `filter`, `filterNot` and `filterIsInstance` functions for `Assertion.Builder<Iterable<*>>`

## 0.17.0 Forsaken Raven - 2018-11-03

- Upgrades Strikt to depend on Kotlin 1.3.
- `expectThrows` is un-deprecated.
- Expectations are now compatible with suspending lambdas.
- `isBefore` and `isAfter` in `strikt-java-time` now support `ZonedDateTime`.

## 0.16.3 Saffron Mammoth - 2018-10-26

- `Assertion.Builder<Collection<*>.isSorted`
- `isEqualTo` now has special handling for arrays that compares content using `contentEquals`.

## 0.16.2 Anonymous Ifrit - 2018-10-19

- Backed out truncation of values in failure messages.

## 0.16.1 Onyx Mammoth - 2018-10-19

- Backed out truncation of values in failure messages.

## 0.16.0 Sadness Blossom - 2018-10-11

- Assertions for `java.time` types moved from `strikt-core` to new `strikt-java-time` module.
- `Assertion.Builder<Map<*, *>>.isNotEmpty`
- `Assertion.Builder<Array<*>>.toList`
- `Assertion.Builder<CharSequence>.trim`
- `Assertion.Builder<String>.trim`

## 0.15.2 Alabaster Tyrant - 2018-10-08

- Added `is(Not)(NullOr)(Empty|Blank)` assertions.

## 0.15.1 Wasteland Monkey - 2018-10-05

- Changes `get` to use a receiver rather than a parameter.
- Adds new `first(predicate)` and `flatMap` mapping extensions for iterable subjects.

## 0.15.0 Garden Daimyo - 2018-10-02

- Deprecates `chain` in favor of new `get` method.

## 0.14.4 Void Maiden - 2018-09-29

- Added Java 11 build & hopefully fixed site publishing

## 0.14.3 Hungry Boulder - 2018-09-27

- Added `isIn` assertion.

## 0.14.2 Black Waterfall - 2018-09-24

## 0.14.1 Bitter Tiger - 2018-09-22

Fixes broken dependency declaration in `strikt-protobuf`.

## 0.14.0 Ruby Panther - 2018-09-22

Breaking change renaming `map` to `chain` in order to allow a map method that applies to `Assertion.Builder<Iterable<*>>`.

## 0.13.0 Blessed Starfall - 2018-09-16

- Revisions to exception assertions.
  - Instead of `assertThrows(() -> Unit)` use `assertThat(catching(() -> Unit))`
  - throws' receiver is now `Assertion.Builder<Throwable?>` instead of `Assertion.Builder<() -> Unit>`

## 0.12.0 Adamant Rose - 2018-09-13

## 0.11.6 Moonsilver Serpent - 2018-09-06

## 0.11.5 Harmonious Eagle - 2018-08-25

## 0.11.4 Pain Empress - 2018-08-24

## 0.11.3 Unfettered Sky - 2018-08-23

## 0.11.2 Viper Understanding - 2018-08-18

## 0.11.1 Mantis Discipline - 2018-08-17

## 0.11.0 Ascending Snake - 2018-08-14

## 0.10.2 Forest Daimyo - 2018-08-12

## 0.10.1 Silhouette Evasion - 2018-08-11

## 0.10.0 Ashen Intuition - 2018-08-09

## 0.9.0 Glorious Bitterness - 2018-07-15

## 0.8.6 Fluttering Rat - 2018-07-15

## 0.8.5 Havoc Lotus - 2018-07-10

## 0.8.4 Vengeful Technique - 2018-06-28

## 0.8.3 Scorning Guardian - 2018-06-11

## 0.8.2 Ebon Claw - 2018-06-11

## 0.8.1 Marginal Spring - 2018-06-10

## 0.8.0 Ridiculous Diplomat - 2018-06-08

## 0.7.4 Stalwart Havoc - 2018-06-05

## 0.7.2 Noticeable Bell - 2018-05-31

## 0.7.1 Bright Raven - 2018-05-31

## 0.7.0 Vermillion Mantis - 2018-05-31

## 0.6.8 Battleworn Hyena - 2018-05-29

## 0.6.7 Black Rampart - 2018-05-29

## 0.6.6 Quicksilver Serpent - 2018-05-29

## 0.6.5 Whispering Horizon - 2018-05-29

## 0.6.4 Sorrowful Bow - 2018-05-25

## 0.6.3 Flaxen Tiger - 2018-05-25

## 0.6.2 Benificent Locust - 2018-05-25

## 0.6.1 Drunken Orchid - 2018-05-21

## 0.6.0 Metal Eagle - 2018-05-21

## 0.5.1 Tsunami Carp - 2018-05-19

## 0.5.0 Saffron Thunderhead - 2018-05-19

## 0.4.2 Virtuous Monsoon - 2018-05-16

## 0.4.1 Iron Mind - 2018-05-16

## 0.4.0 Starmetal Mist - 2018-05-15

## 0.3.0 Towering Moon - 2018-05-14

## 0.2.7 Elder Starfall - 2018-05-10

## 0.2.6 Sapphire Spear - 2018-05-10

## 0.2.5 - 2018-05-10

## 0.2.4 - 2018-05-09

## 0.2.3 - 2018-05-09

## 0.2.2 - 2018-05-05

## 0.2.1 - 2018-05-04

## 0.2.0 - 2018-05-04

## 0.1.0 - 2018-05-03
