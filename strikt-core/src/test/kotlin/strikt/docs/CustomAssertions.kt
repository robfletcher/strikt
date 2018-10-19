package strikt.docs

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.throws
import java.time.LocalDate
import java.time.MonthDay

@DisplayName("Snippets used in Orchid docs")
internal class CustomAssertions {

// custom-assertions.md
// -----------------------------------------------------------------------------

  @Test fun `custom assertions 1, 2`() {
    // START custom_assertions_1
    fun Assertion.Builder<LocalDate>.isStTibsDay(): Assertion.Builder<LocalDate> =
      assert("is St. Tib's Day") {
        when (MonthDay.from(it)) {
          MonthDay.of(2, 29) -> pass()
          else -> fail()
        }
      }
    // END custom_assertions_1

    // START custom_assertions_2
    val s = """ // IGNORE
    ▼ Expect that 2018-05-01:
      ✗ is St. Tib's Day
    """ // IGNORE
    // END custom_assertions_2

    expectThat(catching {
      expectThat(LocalDate.of(2018, 5, 1)).isStTibsDay()
    }).throws<AssertionFailedError>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

  @Test fun `custom assertions 3, 4`() {
    // START custom_assertions_3
    fun Assertion.Builder<LocalDate>.isStTibsDay(): Assertion.Builder<LocalDate> =
      assert("is St. Tib's Day") {
        when (MonthDay.from(it)) {
          MonthDay.of(2, 29) -> pass()
          else -> fail(
            description = "in fact it is %s",
            actual = it
          )
        }
      }
    // END custom_assertions_3

    // START custom_assertions_4
    val s = """ // IGNORE
    ▼ Expect that 2018-05-01:
      ✗ is St. Tib's Day : in fact it is 2018-05-01
    """ // IGNORE
    // END custom_assertions_4

    expectThat(catching {
      expectThat(LocalDate.of(2018, 5, 1)).isStTibsDay()
    }).throws<AssertionFailedError>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

  @Test fun `custom assertions 5`() {
    // START custom_assertions_5
    fun Assertion.Builder<LocalDate>.isStTibsDay(): Assertion.Builder<LocalDate> =
      assertThat("is St. Tib's Day") {
        MonthDay.from(it) == MonthDay.of(2, 29)
      }
    // END custom_assertions_5

    val s = """ // IGNORE
    ▼ Expect that 2018-05-01:
      ✗ is St. Tib's Day
    """ // IGNORE

    expectThat(catching {
      expectThat(LocalDate.of(2018, 5, 1)).isStTibsDay()
    }).throws<AssertionFailedError>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }

  @Test fun `custom assertions 6`() {
    // START custom_assertions_6
    fun <T : Iterable<E?>, E> Assertion.Builder<T>.containsNoNullElements(): Assertion.Builder<T> =
      compose("does not contain any null elements") { subject ->
        subject.forEach { element ->
          get("%s") { element }.isNotNull()
        }
      } then {
        if (allPassed) pass() else fail()
      }
    // END custom_assertions_6

    // START custom_assertions_7
    val s = """ // IGNORE
    ▼ Expect that ["catflap", null, "rubberplant", "marzipan"]:
      ✗ does not contain any null elements
        ▼ "catflap":
          ✓ is not null
        ▼ null:
          ✗ is not null
        ▼ "rubberplant":
          ✓ is not null
        ▼ "marzipan":
          ✓ is not null
    """ // IGNORE
    // END custom_assertions_7

    val subject = listOf("catflap", null, "rubberplant", "marzipan")

    expectThat(catching {
      expectThat(subject).containsNoNullElements()
    }).throws<AssertionFailedError>()
      .get { message }
      .isEqualTo(s.replace(" // IGNORE", "").trimIndent().trim())
  }
}
