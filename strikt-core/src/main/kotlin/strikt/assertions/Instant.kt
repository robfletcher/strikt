package strikt.assertions

import strikt.api.Assertion
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetTime
import java.time.Year
import java.time.YearMonth
import java.time.chrono.ChronoLocalDate
import java.time.temporal.TemporalAccessor

/**
 * Asserts that the subject `Instant` is before [expected].
 *
 * @throws java.time.DateTimeException if [expected] cannot be converted to an
 * `Instant` by [Instant.from].
 */
fun <T : TemporalAccessor> Assertion.Builder<T>.isBefore(expected: TemporalAccessor): Assertion.Builder<T> =
  passesIf("is before %s", expected) {
    when (this) {
      is Instant -> isBefore(Instant.from(expected))
      is ChronoLocalDate -> isBefore(LocalDate.from(expected))
      is LocalTime -> isBefore(LocalTime.from(expected))
      is MonthDay -> isBefore(MonthDay.from(expected))
      is OffsetTime -> isBefore(OffsetTime.from(expected))
      is Year -> isBefore(Year.from(expected))
      is YearMonth -> isBefore(YearMonth.from(expected))
      else -> throw UnsupportedOperationException()
    }
  }
