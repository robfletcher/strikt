package strikt.assertions

import com.oneeyedmen.minutest.Context
import com.oneeyedmen.minutest.junit.testFactoryFor
import com.oneeyedmen.minutest.rootContext
import org.junit.jupiter.api.DynamicNode
import strikt.api.Assertion
import java.util.stream.Stream

fun <S : Any?> assertionTests(builder: Context<Unit, Assertion.Builder<S>>.() -> Unit): Stream<out DynamicNode> =
  testFactoryFor(
    rootContext(builder = builder)
  )

fun Any?.quoted(): String = when (this) {
  null -> "null"
  else -> "\"$this\""
}
