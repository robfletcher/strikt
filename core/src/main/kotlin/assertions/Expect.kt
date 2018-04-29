package assertions

fun expect(target: Any?, block: Assertion<Any>.() -> Unit) {
  Assertion(target).block()
}

fun <T : Any> expect(target: T?): Assertion<T> = Assertion(target)

fun <T : CharSequence> expect(target: T?, block: CharSequenceAssertion<T>.() -> Unit) {
  CharSequenceAssertion(target).block()
}

fun <T : CharSequence> expect(target: T?): CharSequenceAssertion<T> = CharSequenceAssertion(target)
