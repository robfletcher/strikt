package strikt.assertions

import com.oneeyedmen.minutest.TestContext
import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.DynamicNode
import strikt.api.Assertion
import java.util.stream.Stream

fun <S : Any?> assertionTests(builder: TestContext<Assertion.Builder<S>>.() -> Unit): Stream<out DynamicNode> =
  junitTests(builder)

fun Any?.quoted(): String = when (this) {
  null -> "null"
  else -> "\"$this\""
}
