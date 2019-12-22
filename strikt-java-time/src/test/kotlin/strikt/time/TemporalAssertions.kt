package strikt.time

import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.chrono.JapaneseDate
import java.time.temporal.ChronoField.MILLI_OF_SECOND
import java.time.temporal.ChronoField.SECOND_OF_MINUTE
import java.time.temporal.ChronoField.YEAR
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalField
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("assertions on temporal types")
internal class TemporalAssertions {

  companion object {
    val zone: ZoneId = ZoneId.systemDefault()
    val now: Instant = Instant.now()
    val local: ZonedDateTime = now.atZone(zone)
    val today: LocalDate = local.toLocalDate()
    val rightNow: LocalTime = local.toLocalTime()
  }

  @Nested
  @DisplayName("isBefore assertion")
  inner class IsBefore {
    @TestFactory
    fun `passes if subject value is before the expected`() =
      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(now, now.plusMillis(1)),
        Pair(now.atOffset(ZoneOffset.MIN).toInstant(), now.plusMillis(1)),
        Pair(now.atOffset(ZoneOffset.UTC).toInstant(), now.plusMillis(1)),
        Pair(now.atOffset(ZoneOffset.MAX).toInstant(), now.plusMillis(1)),
        Pair(now, now.plusMillis(1).atOffset(ZoneOffset.MIN)),
        Pair(now, now.plusMillis(1).atOffset(ZoneOffset.UTC)),
        Pair(now, now.plusMillis(1).atOffset(ZoneOffset.MAX)),
        Pair(today, today.plusDays(1)),
        Pair(JapaneseDate.from(today), today.plusDays(1)),
        Pair(rightNow, rightNow.plusSeconds(1)), // TODO: potential failure on day boundary
        Pair(rightNow, now.plusSeconds(1).atZone(zone)), // TODO: potential failure on day boundary
        Pair(MonthDay.from(today), MonthDay.from(today.plusDays(1))),
        Pair(MonthDay.from(today), today.plusDays(1)),
        Pair(OffsetTime.from(local), local.plusSeconds(1)), // TODO: potential failure on day boundary
        Pair(Year.from(today), today.plusYears(1)),
        Pair(YearMonth.from(today), today.plusMonths(1)),
        local to local.plusDays(1)
      ).map { (subject, expected) ->
        dynamicTest("passes asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          expectThat(subject).isBefore(expected)
        }
      }

    @TestFactory
    fun `fails if the subject value is the same or later than the expected`(): List<DynamicTest> {
      return listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(now, now),
        Pair(now.atOffset(ZoneOffset.MIN).toInstant(), now),
        Pair(now.atOffset(ZoneOffset.UTC).toInstant(), now),
        Pair(now.atOffset(ZoneOffset.MAX).toInstant(), now),
        Pair(now, now.atOffset(ZoneOffset.MIN)),
        Pair(now, now.atOffset(ZoneOffset.UTC)),
        Pair(now, now.atOffset(ZoneOffset.MAX)),
        Pair(now, now.minusMillis(1)),
        Pair(today, today),
        Pair(today, today.minusDays(1)),
        Pair(JapaneseDate.from(today), today.minusDays(1)),
        Pair(rightNow, rightNow),
        Pair(rightNow, rightNow.minusSeconds(1)), // TODO: potential failure on day boundary
        Pair(rightNow, now.atZone(zone)),
        Pair(rightNow, now.minusSeconds(1).atZone(zone)), // TODO: potential failure on day boundary
        Pair(MonthDay.from(today), MonthDay.from(today)),
        Pair(MonthDay.from(today), MonthDay.from(today.minusDays(1))),
        Pair(MonthDay.from(today), today),
        Pair(MonthDay.from(today), today.minusDays(1)),
        Pair(OffsetTime.from(local), local),
        Pair(OffsetTime.from(local), local.minusSeconds(1)), // TODO: potential failure on day boundary
        Pair(Year.from(today), today),
        Pair(Year.from(today), today.minusYears(1)),
        Pair(YearMonth.from(today), today),
        Pair(YearMonth.from(today), today.minusMonths(1)),
        local to local.minusDays(1)
      ).map { (subject, expected) ->
        dynamicTest("fails asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          assertThrows<AssertionFailedError> {
            expectThat(subject).isBefore(expected)
          }
        }
      }
    }

    @TestFactory
    fun `throws an exception if expected value can't be converted to the subject type`() =
      listOf<Pair<Instant, TemporalAccessor>>(
        Pair(now, LocalDate.of(2008, 10, 2))
      ).map { (subject, expected) ->
        dynamicTest("fails asserting $subject is before $expected") {
          assertThrows<DateTimeException> {
            expectThat(subject).isBefore(expected)
          }
        }
      }
  }

  @Nested
  @DisplayName("isAfter assertion")
  inner class IsAfter {
    @TestFactory
    fun `passes if subject value is after the expected`() =
      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(now, now.minusMillis(1)),
        Pair(now.atOffset(ZoneOffset.MIN).toInstant(), now.minusMillis(1)),
        Pair(now.atOffset(ZoneOffset.UTC).toInstant(), now.minusMillis(1)),
        Pair(now.atOffset(ZoneOffset.MAX).toInstant(), now.minusMillis(1)),
        Pair(now, now.minusMillis(1).atOffset(ZoneOffset.MIN)),
        Pair(now, now.minusMillis(1).atOffset(ZoneOffset.UTC)),
        Pair(now, now.minusMillis(1).atOffset(ZoneOffset.MAX)),
        Pair(today, today.minusDays(1)),
        Pair(JapaneseDate.from(today), today.minusDays(1)),
        Pair(rightNow, rightNow.minusSeconds(1)), // TODO: potential failure on day boundary
        Pair(rightNow, now.minusSeconds(1).atZone(zone)), // TODO: potential failure on day boundary
        Pair(MonthDay.from(today), MonthDay.from(today.minusDays(1))),
        Pair(MonthDay.from(today), today.minusDays(1)),
        Pair(OffsetTime.from(local), local.minusSeconds(1)), // TODO: potential failure on day boundary
        Pair(Year.from(today), today.minusYears(1)),
        Pair(YearMonth.from(today), today.minusMonths(1)),
        local to local.minusDays(1)
      ).map { (subject, expected) ->
        dynamicTest("passes asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          expectThat(subject).isAfter(expected)
        }
      }

    @TestFactory
    fun `fails if the subject value is the same or earlier than the expected`(): List<DynamicTest> {
      return listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(now, now),
        Pair(now.atOffset(ZoneOffset.MIN).toInstant(), now),
        Pair(now.atOffset(ZoneOffset.UTC).toInstant(), now),
        Pair(now.atOffset(ZoneOffset.MAX).toInstant(), now),
        Pair(now, now.atOffset(ZoneOffset.MIN)),
        Pair(now, now.atOffset(ZoneOffset.UTC)),
        Pair(now, now.atOffset(ZoneOffset.MAX)),
        Pair(now, now.plusMillis(1)),
        Pair(today, today),
        Pair(today, today.plusDays(1)),
        Pair(JapaneseDate.from(today), today.plusDays(1)),
        Pair(rightNow, rightNow),
        Pair(rightNow, rightNow.plusSeconds(1)), // TODO: potential failure on day boundary
        Pair(rightNow, now.atZone(zone)),
        Pair(rightNow, now.plusSeconds(1).atZone(zone)), // TODO: potential failure on day boundary
        Pair(MonthDay.from(today), MonthDay.from(today)),
        Pair(MonthDay.from(today), MonthDay.from(today.plusDays(1))),
        Pair(MonthDay.from(today), today),
        Pair(MonthDay.from(today), today.plusDays(1)),
        Pair(OffsetTime.from(local), local),
        Pair(OffsetTime.from(local), local.plusSeconds(1)), // TODO: potential failure on day boundary
        Pair(Year.from(today), today),
        Pair(Year.from(today), today.plusYears(1)),
        Pair(YearMonth.from(today), today),
        Pair(YearMonth.from(today), today.plusMonths(1)),
        local to local.plusDays(1)
      ).map { (subject, expected) ->
        dynamicTest("fails asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          assertThrows<AssertionFailedError> {
            expectThat(subject).isAfter(expected)
          }
        }
      }
    }

    @TestFactory
    fun `throws an exception if expected value can't be converted to the subject type`() =
      listOf<Pair<Instant, TemporalAccessor>>(
        Pair(now, LocalDate.of(2008, 10, 2))
      ).map { (subject, expected) ->
        dynamicTest("fails asserting $subject is before $expected") {
          assertThrows<DateTimeException> {
            expectThat(subject).isBefore(expected)
          }
        }
      }
  }

  @Nested
  @DisplayName("get(TemporalField) mapping")
  inner class GetMapping {
    @TestFactory
    fun `maps to the int value of the specified field`() =
      listOf<Triple<TemporalAccessor, TemporalField, Int>>(
        Triple(now, MILLI_OF_SECOND, now.get(MILLI_OF_SECOND)),
        Triple(local, YEAR, Year.from(today).value),
        Triple(local, SECOND_OF_MINUTE, local.second),
        Triple(today, YEAR, Year.from(today).value)
      ).map { (subject, field, expected) ->
        dynamicTest("maps $subject (${subject.javaClass.simpleName}) $field to $expected (Int)") {
          expectThat(subject).get(field).isEqualTo(expected)
        }
      }
  }

  @Nested
  @DisplayName("getLong(TemporalField) mapping")
  inner class GetLongMapping {
    @TestFactory
    fun `maps to the long value of the specified field`() =
      listOf<Triple<TemporalAccessor, TemporalField, Long>>(
        Triple(now, MILLI_OF_SECOND, now.getLong(MILLI_OF_SECOND)),
        Triple(local, YEAR, Year.from(today).value.toLong()),
        Triple(local, SECOND_OF_MINUTE, local.second.toLong()),
        Triple(today, YEAR, Year.from(today).value.toLong())
      ).map { (subject, field, expected) ->
        dynamicTest("maps $subject (${subject.javaClass.simpleName}) $field to $expected (Long)") {
          expectThat(subject).getLong(field).isEqualTo(expected)
        }
      }
  }
}
