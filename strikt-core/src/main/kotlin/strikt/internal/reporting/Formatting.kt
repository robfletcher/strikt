package strikt.internal.reporting

import kotlin.jvm.internal.CallableReference
import strikt.internal.ComparedValues

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
    is CharSequence -> "\"$value\""
    is Char -> "'$value'"
    is Iterable<*> -> if (value.javaClass.preferToString()) value.toString() else value.map(::formatValue)
    is Byte -> "0x${value.toString(16)}"
    is ByteArray -> "0x${value.toHex()}".truncate()
    is CharArray -> formatValue(value.toList())
    is ShortArray -> formatValue(value.toList())
    is IntArray -> formatValue(value.toList())
    is LongArray -> formatValue(value.toList())
    is FloatArray -> formatValue(value.toList())
    is DoubleArray -> formatValue(value.toList())
    is Array<*> -> formatValue(value.toList())
    is Class<*> -> value.name
    is Regex -> "/${value.pattern}/"
    is Throwable -> value.javaClass.name
    is CallableReference -> "${formatValue(value.boundReceiver)}::${value.name}"
    is Pair<*, *> -> "{${formatValue(value.first)}: ${formatValue(value.second)}}"
    is Map<*, *> -> value.map { (k, v) -> formatValue(k) to formatValue(v) }.toMap()
    is Number -> value
    else -> value.toString()
  }

private fun Class<*>.preferToString(): Boolean =
  getMethod("toString").declaringClass == this

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

internal const val FORMATTED_VALUE_MAX_LENGTH = 40

private fun CharSequence.truncate(maxLength: Int = FORMATTED_VALUE_MAX_LENGTH) =
  when (length) {
    in 0..maxLength -> this
    else -> substring(0, maxLength) + "…"
  }
