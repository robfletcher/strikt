package kirk.internal

internal data class Described<T>(
  val description: String,
  val value: T
) {
  internal constructor(value: T) : this("%s", value)

  override fun toString(): String = String.format(description, value)
}