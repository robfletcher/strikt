<!doctype html>
<html lang="en" class="has-spaced-navbar-fixed-top">
<head>
  <#include "head.ftl">
  <style>
    .notification code, .notification pre {
      background-color: transparent;
    }

    .is-gapless .column .notification {
      border-top-left-radius: 0;
      border-top-right-radius: 0;
      border-bottom-left-radius: 0;
      border-bottom-right-radius: 0;
    }

    @media screen and (max-width: 768px) {
      .is-4:first-child .notification {
        border-top-left-radius: 4px;
        border-top-right-radius: 4px;
      }

      .is-8:last-child .notification {
        border-bottom-left-radius: 4px;
        border-bottom-right-radius: 4px;
      }
    }

    @media screen and (min-width: 769px) {
      .column .notification,
      .column .image {
        height: 100%;
      }

      .is-4:first-child .notification {
        border-top-left-radius: 4px;
      }

      .is-8:nth-child(2) .notification {
        border-top-right-radius: 4px;
      }

      .is-4:nth-last-child(2) .notification {
        border-bottom-left-radius: 4px;
      }

      .is-8:last-child .notification {
        border-bottom-right-radius: 4px;
      }
    }
  </style>
</head>
<body>
  <#include "navbar.ftl">
<main class="section">
  <div class="container">

    <div class="columns is-centered">
      <div class="column is-10-desktop">

        <div class="columns">
          <div class="column is-8">
            <figure class="image is-4by1">
              <img src="/img/logo.png" alt="Strikt logo">
            </figure>
          </div>

          <div class="column is-4">
            <div class="notification is-link content is-medium">
              <p>
                Strikt is an assertion library for Kotlin intended for use with a test runner such as <a
                href="https://junit.org/junit5/">JUnit</a> or <a href="http://spekframework.org/">Spek</a>.
              </p>
              <p>Strikt gives you…</p>
            </div>
          </div>
        </div>

        <div class="columns is-multiline is-gapless">
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <h2 class="title">A powerful fluent API</h2>
              <p>Type-safe fluent assertions</p>
            </div>
          </div>
          <div class="column is-8">
          <pre class="notification is-dark"><code>val subject: "The Enlightened take things Lightly"
expect(subject)
  .hasLength(35)
  .matches(Regex("[\w\s]+"))
  .startsWith("T")</code></pre>
          </div>
        </div>

        <div class="columns is-multiline is-gapless">
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <h2 class="title">Collection handling</h2>
              <p>Flexible assertions about collections</p>
            </div>
          </div>
          <div class="column is-8">
          <pre class="notification is-dark"><code>expect(subject)
  .contains("Eris", "Thor", "Anubis")</code></pre>
          </div>

          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <p>&ldquo;Narrow&rdquo; the assertion to elements or ranges</p>
            </div>
          </div>
          <div class="column is-8">
            <pre class="notification is-dark"><code>expect(subject)[0].isEqualTo("Eris")</code></pre>
          </div>

          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <p>Make grouping assertions</p>
            </div>
          </div>
          <div class="column is-8">
          <pre class="notification is-dark"><code>val subject = Pantheon.values()
expect(subject)
  .isNotEmpty()
  .any { startsWith("E") }</code></pre>
          </div>
        </div>

        <div class="columns is-multiline is-gapless">
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <h2 class="title">&ldquo;Soft&rdquo; assertions</h2>
              <p>Use lambdas to execute multiple assertions on a subject at once&hellip;</p>
            </div>
          </div>
          <div class="column is-8">
        <pre class="notification is-dark"><code>val subject: "The Enlightened take things Lightly"
expect(subject) {
  hasLength(5)          // fails
  matches(Regex("\d+")) // fails
  startsWith("T")       // still evaluated and passes
}</code></pre>
          </div>
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <p>&hellip;with structured diagnostics of those that fail</p>
            </div>
          </div>
          <div class="column is-8">
        <pre class="notification is-dark"><code>Expect that "The Enlightened take things Lightly" (2 failures)
  has length 5 : found 35
  matches the regular expression /\d+/</code></pre>
          </div>
        </div>

        <div class="columns is-multiline is-gapless">
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <h2 class="title">Strong typing</h2>
              <p>Assertion functions can "narrow" the type of the assertion</p>
            </div>
          </div>
          <div class="column is-8">
          <pre class="notification is-dark"><code>val subject: Any? = "The Enlightened take things Lightly"
expect(subject)                // type: Assertion&lt;Any?&gt;
  .isNotNull()                 // type: Assertion&lt;Any&gt;
  .isA&lt;String&gt;()               // type: Assertion&lt;String&gt;
  .matches(Regex("[\\w\\s]+"))
  // only available on Assertion&lt;CharSequence&gt;</code></pre>
          </div>
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <p>Assertions can "map" to properties and method results in a type safe way:</p>
            </div>
          </div>
          <div class="column is-8">
          <pre class="notification is-dark"><code>val subject = Pantheon.ERIS
expect(subject)
  .map(Deity::realm)  // reference to a property
  .map { toString() } // return type of a method call
  .isEqualTo("discord and confusion")</code></pre>
          </div>
        </div>

        <div class="columns is-multiline is-gapless">
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <h2 class="title">Extensibility</h2>
              <p>Custom assertions are extension functions</p>
            </div>
          </div>
          <div class="column is-8">
        <pre class="notification is-dark"><code>fun Assertion&lt;LocalDate&gt;.isStTibsDay() =
  assert("is St. Tib's Day") {
    when (MonthDay.from(subject)) {
      MonthDay.of(2, 29) -> pass()
      else               -> fail()
    }
  }

expect(LocalDate.of(2018, 5, 15)).isStTibsDay()</code></pre>
          </div>
          <div class="column is-4 has-text-right-tablet">
            <div class="notification">
              <p>Custom mappings are extension properties</p>
            </div>
          </div>
          <div class="column is-8">
        <pre class="notification is-dark"><code>val Assertion&lt;Deity&gt;.realm: Assertion&lt;String&gt;
  get() = map(Deity::realm)

val subject = Pantheon.ERIS
expect(subject).realm.isEqualTo("discord and confusion")</code></pre>
          </div>
        </div>
      </div>
    </div>

  </div>

</main>
  <#include "footer.ftl">
</body>
</html>
