package strikt.api.reporting

import kotlin.jvm.internal.CallableReference

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
