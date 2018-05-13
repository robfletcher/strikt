package kirk.internal

data class Described<T>(
  val description: String,
  val value: T
) {
  override fun toString() = String.format(description, value)
}