package strikt.assertions

import com.oneeyedmen.minutest.Context
import com.oneeyedmen.minutest.TestContext
import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.DynamicNode
import strikt.api.Assertion
import java.util.stream.Stream

fun Any.striktTests(builder: TestContext<Unit>.() -> Unit): Stream<out DynamicNode> =
  junitTests(builder)

fun <S: Any?> TestContext<Unit>.subjectContext(name: String, builder: Context<Unit, Assertion.Builder<S>>.() -> Unit) =
  derivedContext(name, builder)

fun <S : Any?> Any.assertionTests(builder: TestContext<Assertion.Builder<S>>.() -> Unit): Stream<out DynamicNode> =
  junitTests(builder)

fun Any?.quoted(): String = when (this) {
  null -> "null"
  else -> "\"$this\""
}
