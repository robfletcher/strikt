package assertions

fun <T : Any?> expect(target: T, block: T.() -> Unit) {
  target.block()
}

