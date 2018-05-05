## Publishing

Cut a regular (minor) release:

```
gradle final
```

Cut a major patch release:

```
gradle final -Prelease.scope=major
gradle final -Prelease.scope=patch
```

Fucksocks, the last publish didn't work. Let me do it again:

```
gradle final -Prelease.disableGitChecks=true -Prelease.useLastTag=true
```

Force a version number for some reason:

```
gradle final -Prelease.version=1.2.3
```