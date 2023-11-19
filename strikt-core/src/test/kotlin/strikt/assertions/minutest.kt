package strikt.assertions

import dev.minutest.TestContextBuilder
import dev.minutest.junit.testFactoryFor
import dev.minutest.rootContext
import org.junit.jupiter.api.DynamicNode
import strikt.api.Assertion
import java.util.stream.Stream

inline fun <reified S : Any?> testFactory(noinline builder: TestContextBuilder<Unit, S>.() -> Unit): Stream<out DynamicNode> =
  testFactoryFor(
    rootContext(builder = builder)
  )

fun <S : Any?> assertionTests(builder: TestContextBuilder<Unit, Assertion.Builder<S>>.() -> Unit): Stream<out DynamicNode> =
  testFactory(builder)

fun Any?.quoted(): String =
  when (this) {
    null -> "null"
    else -> "\"$this\""
  }
