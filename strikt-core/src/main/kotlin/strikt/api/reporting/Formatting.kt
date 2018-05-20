package strikt.api.reporting

fun formatValue(value: Any?): Any =
  when (value) {
    null            -> "null"
    is CharSequence -> "\"$value\""
    is Char         -> "'$value'"
    is Iterable<*>  -> value.map(::formatValue)
    is Class<*>     -> value.name
    is Regex        -> "/${value.pattern}/"
      is Throwable  -> value.javaClass.name
    else            -> value
  }
