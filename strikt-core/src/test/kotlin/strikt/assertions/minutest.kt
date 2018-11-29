package strikt.assertions

import com.oneeyedmen.minutest.Context
import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.DynamicNode
import strikt.api.Assertion
import java.util.stream.Stream

fun <S : Any?> Any.assertionTests(builder: Context<Unit, Assertion.Builder<S>>.() -> Unit): Stream<out DynamicNode> =
  junitTests(builder = builder)

fun Any?.quoted(): String = when (this) {
  null -> "null"
  else -> "\"$this\""
}
