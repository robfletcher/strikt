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
import java.time.temporal.TemporalField

/**
 * Asserts that the subject is before [expected].
 *
 * @throws java.time.DateTimeException if [expected] is not a compatible
 * temporal type.
 */
fun <T : TemporalAccessor> Assertion.Builder<T>.isBefore(expected: TemporalAccessor): Assertion.Builder<T> =
  passesIf("is before %s", expected) {
    when (it) {
      is Instant -> it.isBefore(Instant.from(expected))
      is ChronoLocalDate -> it.isBefore(LocalDate.from(expected))
      is LocalTime -> it.isBefore(LocalTime.from(expected))
      is MonthDay -> it.isBefore(MonthDay.from(expected))
      is OffsetTime -> it.isBefore(OffsetTime.from(expected))
      is Year -> it.isBefore(Year.from(expected))
      is YearMonth -> it.isBefore(YearMonth.from(expected))
      else -> throw UnsupportedOperationException("Strikt's isBefore does not (currently) support ${it.javaClass.simpleName}")
    }
  }

/**
 * Asserts that the subject is after [expected].
 *
 * @throws java.time.DateTimeException if [expected] is not a compatible
 * temporal type.
 */
fun <T : TemporalAccessor> Assertion.Builder<T>.isAfter(expected: TemporalAccessor): Assertion.Builder<T> =
  passesIf("is before %s", expected) {
    when (it) {
      is Instant -> it.isAfter(Instant.from(expected))
      is ChronoLocalDate -> it.isAfter(LocalDate.from(expected))
      is LocalTime -> it.isAfter(LocalTime.from(expected))
      is MonthDay -> it.isAfter(MonthDay.from(expected))
      is OffsetTime -> it.isAfter(OffsetTime.from(expected))
      is Year -> it.isAfter(Year.from(expected))
      is YearMonth -> it.isAfter(YearMonth.from(expected))
      else -> throw UnsupportedOperationException("Strikt's isAfter does not (currently) support ${it.javaClass.simpleName}")
    }
  }

/**
 * Maps an assertion on the subject to an assertion on the value of the
 * specified temporal field.
 *
 * @throws java.time.temporal.UnsupportedTemporalTypeException if the subject
 * does not support the [field] type.
 *
 * @see TemporalAccessor.get
 */
fun <T : TemporalAccessor> Assertion.Builder<T>.get(field: TemporalField): Assertion.Builder<Int> =
  get(field.toString()) { it.get(field) }

/**
 * Maps an assertion on the subject to an assertion on the value of the
 * specified temporal field.
 *
 * @throws java.time.temporal.UnsupportedTemporalTypeException if the subject
 * does not support the [field] type.
 *
 * @see TemporalAccessor.getLong
 */
fun <T : TemporalAccessor> Assertion.Builder<T>.getLong(field: TemporalField): Assertion.Builder<Long> =
  get(field.toString()) { it.getLong(field) }
