package strikt.exp

import failgood.Test
import failgood.describe
import strikt.api.Assertion
import strikt.api.DescribeableBuilder
import strikt.assertions.endsWith
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.internal.AssertionBuilder
import strikt.internal.AssertionStrategy
import strikt.internal.AssertionSubject

@Suppress("NAME_SHADOWING")
@Test
class HasWithReceiverTest {
  inline fun <T> T.has(function: DSL.(T) -> Unit) {
    DSL().function(this)
  }

  class DSL {
    val errors = mutableListOf<String>()

    @Suppress("UNUSED_VARIABLE")
    val <T> T.that: DescribeableBuilder<T>
      get() {
        val errors = this@DSL.errors // we can access the errors collection in the dsl collector
        return AssertionBuilder(
          AssertionSubject(this), AssertionStrategy.Throwing
        )
      }
    fun <T> Assertion.Builder<T>.andHas(block: DSL.(T) -> Unit) {
      this@DSL.block(this.subject)
    }
  }


  val context = describe("has with receiver") {
    it("works with nested asserts") {
      data class Nested(val field: String)
      data class Role(val nested: Nested)
      data class User(val name: String, val email: String, val role: Role?)

      val user = User("chris", "chris@example.com", Role(Nested("field")))


      user.has { a ->
        a.name.that.isEqualTo("chris")
        a.email.that.endsWith("example.com")
        a.role.that.isNotNull().andHas { a ->
          a.nested.field.that.isEqualTo("field")
        }
      }
    }
    it("can call suspend methods") {
      class Coroutine {
        @Suppress("RedundantSuspendModifier")
        suspend fun otto() = 8
      }
      Coroutine().has { a ->
        a.otto().that.isEqualTo(8)
      }
    }
  }

}


