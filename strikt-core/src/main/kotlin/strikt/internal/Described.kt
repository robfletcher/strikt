package strikt.internal

data class Described<T>(
  val value: T,
  private val description: String = "%s"
) {
  fun describe(formatter: (T) -> Any): String {
    return description.format(formatter(value))
  }
}
