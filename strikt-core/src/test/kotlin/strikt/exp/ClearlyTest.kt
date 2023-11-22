package strikt.exp

import failgood.Test
import failgood.describe
import strikt.api.DescribeableBuilder
import strikt.api.expectThat
import strikt.assertions.endsWith
import strikt.assertions.isEqualTo
import strikt.internal.AssertionBuilder
import strikt.internal.AssertionStrategy
import strikt.internal.AssertionSubject


@Test
class ClearlyTest {
  val context = describe("experimental Any receiver syntax") {
    describe("has") {

      it("works with simple asserts") {
        val kiddo = "jakob"
        // instead of
        expectThat(kiddo).isEqualTo("jakob")
        // we could also write
        kiddo.clearly.isEqualTo("jakob")
        // or likely, doubtless, withoutADoubt, promisesIt, saysIt, you get the idea

        // or using the nested syntax:
        kiddo.has { this.that.isEqualTo("jakob") }
      }
      it("works with nested asserts") {
        data class User(val name: String, val email: String)

        val user = User("chris", "chris@example.com")
        user.has {
          name.that.isEqualTo("chris")
          email.that.endsWith("example.com")
        }
      }
    }
  }
  private val <T> T.clearly: DescribeableBuilder<T>
    get() = AssertionBuilder(
      AssertionSubject(this), AssertionStrategy.Throwing
    )

  private val <T> T.that: DescribeableBuilder<T>
    get() = AssertionBuilder(
      AssertionSubject(this), AssertionStrategy.Throwing
    )

  fun <T> T.has(function: T.() -> Unit) {
    this.function()
  }
}

