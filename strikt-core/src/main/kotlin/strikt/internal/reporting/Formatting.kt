package strikt.internal.reporting

import kotlin.jvm.internal.CallableReference

internal fun formatValues(expected: Any?, actual: Any?): Pair<Any?, Any?> {
  val e = formatValue(expected)
  val a = formatValue(actual)
  return if (e.toString() == a.toString()) {
    Pair(e.withTypeSuffix(expected), a.withTypeSuffix(actual))
  } else {
    Pair(e, a)
  }
}

private fun Any.withTypeSuffix(typeOf: Any?) =
  when (typeOf) {
    null -> this
    else -> "$this (${typeOf.javaClass.kotlin.simpleName})"
  }

internal fun formatValue(value: Any?): Any =
  when (value) {
    null -> "null"
    is CharSequence -> "\"$value\""
    is Char -> "'$value'"
    is Iterable<*> -> value.map(::formatValue)
    is Array<*> -> value.map(::formatValue)
    is Class<*> -> value.name
    is Regex -> "/${value.pattern}/"
    is Throwable -> value.javaClass.name
    is CallableReference -> "${formatValue(value.boundReceiver)}::${value.name}"
    is Pair<*, *> -> "{${formatValue(value.first)}: ${formatValue(value.second)}}"
    else -> value
  }
