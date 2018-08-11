package strikt.assertions

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import strikt.api.expect

@DisplayName("assertions on enums")
internal class EnumAssertions {
  @ParameterizedTest
  @EnumSource(Pantheon::class)
  fun `can map to the enum name"`(deity: Pantheon) {
    expect(deity).name.isEqualTo(deity.name)
  }

  @ParameterizedTest
  @EnumSource(Pantheon::class)
  fun `can map to the enum ordinal`(deity: Pantheon) {
    expect(deity).ordinal.isEqualTo(deity.ordinal)
  }
}

internal enum class Pantheon {
  NORSE, GREEK, ROMAN
}
