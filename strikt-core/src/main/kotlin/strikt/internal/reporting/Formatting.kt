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
    is Iterable<*> -> if (preferToString(value.javaClass)) "\"$value\""
      else
      value.map(::formatValue)
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

fun preferToString(javaClass: Class<*>): Boolean = javaClass.getMethod("toString").declaringClass == javaClass

private val hexArray = "0123456789ABCDEF".toCharArray()
fun ByteArray.toHex(): String {
  val hexChars = CharArray(size * 2)
  for (j in indices) {
    val v: Int = this[j].toInt() and 0xFF
    hexChars[j * 2] = hexArray[v ushr 4]
    hexChars[j * 2 + 1] = hexArray[v and 0x0F]
  }
  return String(hexChars)
}
