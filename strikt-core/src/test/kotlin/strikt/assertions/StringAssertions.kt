package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.internal.opentest4j.CompoundAssertionFailure

@DisplayName("assertions on String")
internal object StringAssertions {
  @TestFactory
  fun isEqualToIgnoringCase() = assertionTests<String> {
    fixture { expectThat("fnord") }

    test("passes if the subject is identical to the expected value") {
      isEqualToIgnoringCase("fnord")
    }

    test("fails if the subject is different") {
      assertThrows<AssertionError> {
        expectThat("despite the negative press fnord")
          .isEqualToIgnoringCase("fnord")
      }
    }

    test("passes if the subject is the same as the expected value apart from case") {
      expectThat("fnord").isEqualToIgnoringCase("fnord")
    }
  }

  @TestFactory
  fun startsWith() = assertionTests<String> {
    fixture { expectThat("fnord") }

    test("can expect string start") {
      expectThat("fnord").startsWith("fno")
    }

    test("outputs real start when startsWith fails") {
      expectThrows<AssertionError> {
        expectThat("fnord").startsWith("fnrd")
      }
        .message
        .isNotNull()
        .contains("""starts with "fnrd" : found "fnor"""")
    }
  }

  @TestFactory
  fun endsWith() = assertionTests<String> {
    fixture { expectThat("fnord") }

    test("can expect string end") {
      expectThat("fnord").endsWith("nord")
    }

    test("outputs real end when endsWith fails") {
      expectThrows<AssertionError> {
        expectThat("fnord").endsWith("nor")
      }
        .message
        .isNotNull()
        .contains("""ends with "nor" : found "ord"""")
    }
  }

  @Test
  fun `can have a block assertion on a string subject without overload clash`() {
    val error = assertThrows<CompoundAssertionFailure> {
      val subject = "The Enlightened take things Lightly"
      expectThat(subject = subject) {
        hasLength(5)
        matches(Regex("\\d+"))
        startsWith("T")
      }
    }
    expectThat(error.failures.size).isEqualTo(2)
  }

  @Test
  fun `can trim string`() {
    expectThat(" fnord ").trim().isEqualToIgnoringCase("fnord")
  }
}
