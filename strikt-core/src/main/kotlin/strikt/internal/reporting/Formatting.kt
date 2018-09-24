package strikt.internal.reporting

import strikt.internal.ComparedValues
import kotlin.jvm.internal.CallableReference

internal fun ComparedValues.formatValues(): ComparedValues {
  val e = formatValue(expected)
  val a = formatValue(actual)
  return if (e.toString() == a.toString()) {
    ComparedValues(e.withTypeSuffix(expected), a.withTypeSuffix(actual))
  } else {
    ComparedValues(e, a)
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
    is CharSequence -> when (value.length) {
      in 0..20 -> "\"$value\""
      else -> "\"${value.substring(0, 20)}...\""
    }
    is Char -> "'$value'"
    is Iterable<*> -> if (value.javaClass.preferToString()) value.toString() else value.map(::formatValue)
    is ByteArray -> "0x${value.toHex()}"
    is CharArray -> value.map(::formatValue)
    is ShortArray -> value.map(::formatValue)
    is IntArray -> value.map(::formatValue)
    is LongArray -> value.map(::formatValue)
    is FloatArray -> value.map(::formatValue)
    is DoubleArray -> value.map(::formatValue)
    is Array<*> -> value.map(::formatValue)
    is Class<*> -> value.name
    is Regex -> "/${value.pattern}/"
    is Throwable -> value.javaClass.name
    is CallableReference -> "${formatValue(value.boundReceiver)}::${value.name}"
    is Pair<*, *> -> "{${formatValue(value.first)}: ${formatValue(value.second)}}"
    else -> value
  }

private fun Class<*>.preferToString(): Boolean = getMethod("toString").declaringClass == this

private val hexArray = "0123456789ABCDEF".toCharArray()
internal fun ByteArray.toHex(): String {
  val hexChars = CharArray(size * 2)
  for (j in indices) {
    val v: Int = this[j].toInt() and 0xFF
    hexChars[j * 2] = hexArray[v ushr 4]
    hexChars[j * 2 + 1] = hexArray[v and 0x0F]
  }
  return String(hexChars)
}
