package strikt.java.time

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
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

internal class TemporalAssertions : JUnit5Minutests {

  companion object {
    val zone: ZoneId = ZoneId.of("America/Los_Angeles")
    val offset: ZoneOffset = ZoneOffset.ofHours(6)
    val instant: Instant = LocalDateTime.of(2019, 11, 29, 16, 43)
      .atZone(zone)
      .toInstant()
    val local: ZonedDateTime = instant.atZone(zone)
    val localOffset: OffsetDateTime = instant.atOffset(offset)
    val date: LocalDate = local.toLocalDate()
    val time: LocalTime = local.toLocalTime()
  }

  fun tests() = rootContext {
    context("isBefore assertion") {
      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(instant, instant.plusMillis(1)),
        Pair(instant.atOffset(ZoneOffset.MIN).toInstant(), instant.plusMillis(1)),
        Pair(instant.atOffset(ZoneOffset.UTC).toInstant(), instant.plusMillis(1)),
        Pair(instant.atOffset(ZoneOffset.MAX).toInstant(), instant.plusMillis(1)),
        Pair(instant, instant.plusMillis(1).atOffset(ZoneOffset.MIN)),
        Pair(instant, instant.plusMillis(1).atOffset(ZoneOffset.UTC)),
        Pair(instant, instant.plusMillis(1).atOffset(ZoneOffset.MAX)),
        Pair(date, date.plusDays(1)),
        Pair(JapaneseDate.from(date), date.plusDays(1)),
        Pair(time, time.plusSeconds(1)),
        Pair(time, instant.plusSeconds(1).atZone(zone)),
        Pair(MonthDay.from(date), MonthDay.from(date.plusDays(1))),
        Pair(MonthDay.from(date), date.plusDays(1)),
        Pair(OffsetTime.from(local), local.plusSeconds(1)),
        Pair(Year.from(date), date.plusYears(1)),
        Pair(YearMonth.from(date), date.plusMonths(1)),
        local to local.plusDays(1),
        Pair(localOffset, localOffset.plusNanos(1))
      ).forEach { (subject, expected) ->
        test("passes asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          expectThat(subject).isBefore(expected)
        }
      }

      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(instant, instant),
        Pair(instant.atOffset(ZoneOffset.MIN).toInstant(), instant),
        Pair(instant.atOffset(ZoneOffset.UTC).toInstant(), instant),
        Pair(instant.atOffset(ZoneOffset.MAX).toInstant(), instant),
        Pair(instant, instant.atOffset(ZoneOffset.MIN)),
        Pair(instant, instant.atOffset(ZoneOffset.UTC)),
        Pair(instant, instant.atOffset(ZoneOffset.MAX)),
        Pair(instant, instant.minusMillis(1)),
        Pair(date, date),
        Pair(date, date.minusDays(1)),
        Pair(JapaneseDate.from(date), date.minusDays(1)),
        Pair(time, time),
        Pair(time, time.minusSeconds(1)),
        Pair(time, instant.atZone(zone)),
        Pair(time, instant.minusSeconds(1).atZone(zone)),
        Pair(MonthDay.from(date), MonthDay.from(date)),
        Pair(MonthDay.from(date), MonthDay.from(date.minusDays(1))),
        Pair(MonthDay.from(date), date),
        Pair(MonthDay.from(date), date.minusDays(1)),
        Pair(OffsetTime.from(local), local),
        Pair(OffsetTime.from(local), local.minusSeconds(1)),
        Pair(Year.from(date), date),
        Pair(Year.from(date), date.minusYears(1)),
        Pair(YearMonth.from(date), date),
        Pair(YearMonth.from(date), date.minusMonths(1)),
        local to local.minusDays(1),
        Pair(localOffset, localOffset.minusNanos(1))
      ).forEach { (subject, expected) ->
        test("fails asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          assertThrows<AssertionFailedError> {
            expectThat(subject).isBefore(expected)
          }
        }
      }

      listOf<Pair<Instant, TemporalAccessor>>(
        Pair(instant, LocalDate.of(2008, 10, 2))
      ).forEach { (subject, expected) ->
        test("fails asserting $subject is before $expected") {
          assertThrows<DateTimeException> {
            expectThat(subject).isBefore(expected)
          }
        }
      }
    }

    context("isSameInstant assertion") {
      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(instant, instant),
        Pair(date, date),
        Pair(JapaneseDate.from(date), date),
        Pair(time, time),
        Pair(MonthDay.from(date), MonthDay.from(date)),
        Pair(local, local),
        Pair(local, localOffset),
        Pair(localOffset, localOffset),
        Pair(localOffset, localOffset.withOffsetSameInstant(ZoneOffset.ofTotalSeconds(offset.totalSeconds + 3600))),
        Pair(OffsetTime.from(local), OffsetTime.from(local)),
        Pair(OffsetTime.from(local), local.toOffsetDateTime()),
        Pair(OffsetTime.from(local), local),
        Pair(Year.from(date), date),
        Pair(YearMonth.from(date), date)
      ).forEach { (subject, expected) ->
        test("passes asserting $subject (${subject.javaClass.simpleName}) is the same instant as $expected (${expected.javaClass.simpleName})") {
          expectThat(subject).isSameInstant(expected)
        }
      }

      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(instant, instant.plusNanos(1)),
        Pair(date, date.plusDays(1)),
        Pair(JapaneseDate.from(date), date.plusDays(1)),
        Pair(time, time.plusNanos(1)),
        Pair(MonthDay.from(date), MonthDay.from(date.plusDays(1))),
        Pair(local, local.plusNanos(1)),
        Pair(local, localOffset.plusNanos(1)),
        Pair(localOffset, localOffset.plusNanos(1)),
        Pair(localOffset, localOffset.withOffsetSameInstant(ZoneOffset.ofTotalSeconds(offset.totalSeconds + 3600)).plusNanos(1)),
        Pair(OffsetTime.from(local), OffsetTime.from(local).plusNanos(1)),
        Pair(OffsetTime.from(local), local.toOffsetDateTime().plusNanos(1)),
        Pair(OffsetTime.from(local), local.plusNanos(1)),
        Pair(Year.from(date), date.plusYears(1)),
        Pair(YearMonth.from(date), date.plusMonths(1))
      ).forEach { (subject, expected) ->
        test("fails asserting $subject (${subject.javaClass.simpleName}) is the same instant as $expected (${expected.javaClass.simpleName})") {
          assertThrows<AssertionFailedError> {
            expectThat(subject).isSameInstant(expected)
          }
        }
      }
    }

    context("isAfter assertion") {
      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(instant, instant.minusMillis(1)),
        Pair(instant.atOffset(ZoneOffset.MIN).toInstant(), instant.minusMillis(1)),
        Pair(instant.atOffset(ZoneOffset.UTC).toInstant(), instant.minusMillis(1)),
        Pair(instant.atOffset(ZoneOffset.MAX).toInstant(), instant.minusMillis(1)),
        Pair(instant, instant.minusMillis(1).atOffset(ZoneOffset.MIN)),
        Pair(instant, instant.minusMillis(1).atOffset(ZoneOffset.UTC)),
        Pair(instant, instant.minusMillis(1).atOffset(ZoneOffset.MAX)),
        Pair(date, date.minusDays(1)),
        Pair(JapaneseDate.from(date), date.minusDays(1)),
        Pair(time, time.minusSeconds(1)),
        Pair(time, instant.minusSeconds(1).atZone(zone)),
        Pair(MonthDay.from(date), MonthDay.from(date.minusDays(1))),
        Pair(MonthDay.from(date), date.minusDays(1)),
        Pair(OffsetTime.from(local), local.minusSeconds(1)),
        Pair(Year.from(date), date.minusYears(1)),
        Pair(YearMonth.from(date), date.minusMonths(1)),
        local to local.minusDays(1),
        Pair(localOffset, localOffset.minusNanos(1))
      ).forEach { (subject, expected) ->
        test("passes asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          expectThat(subject).isAfter(expected)
        }
      }

      listOf<Pair<TemporalAccessor, TemporalAccessor>>(
        Pair(instant, instant),
        Pair(instant.atOffset(ZoneOffset.MIN).toInstant(), instant),
        Pair(instant.atOffset(ZoneOffset.UTC).toInstant(), instant),
        Pair(instant.atOffset(ZoneOffset.MAX).toInstant(), instant),
        Pair(instant, instant.atOffset(ZoneOffset.MIN)),
        Pair(instant, instant.atOffset(ZoneOffset.UTC)),
        Pair(instant, instant.atOffset(ZoneOffset.MAX)),
        Pair(instant, instant.plusMillis(1)),
        Pair(date, date),
        Pair(date, date.plusDays(1)),
        Pair(JapaneseDate.from(date), date.plusDays(1)),
        Pair(time, time),
        Pair(time, time.plusSeconds(1)),
        Pair(time, instant.atZone(zone)),
        Pair(time, instant.plusSeconds(1).atZone(zone)),
        Pair(MonthDay.from(date), MonthDay.from(date)),
        Pair(MonthDay.from(date), MonthDay.from(date.plusDays(1))),
        Pair(MonthDay.from(date), date),
        Pair(MonthDay.from(date), date.plusDays(1)),
        Pair(OffsetTime.from(local), local),
        Pair(OffsetTime.from(local), local.plusSeconds(1)),
        Pair(Year.from(date), date),
        Pair(Year.from(date), date.plusYears(1)),
        Pair(YearMonth.from(date), date),
        Pair(YearMonth.from(date), date.plusMonths(1)),
        local to local.plusDays(1),
        Pair(localOffset, localOffset.plusNanos(1))
      ).forEach { (subject, expected) ->
        test("fails asserting $subject (${subject.javaClass.simpleName}) is before $expected (${expected.javaClass.simpleName})") {
          assertThrows<AssertionFailedError> {
            expectThat(subject).isAfter(expected)
          }
        }
      }

      listOf<Pair<Instant, TemporalAccessor>>(
        Pair(instant, LocalDate.of(2008, 10, 2))
      ).forEach { (subject, expected) ->
        test("fails asserting $subject is before $expected") {
          assertThrows<DateTimeException> {
            expectThat(subject).isBefore(expected)
          }
        }
      }
    }

    context("get(TemporalField) mapping") {
      listOf<Triple<TemporalAccessor, TemporalField, Int>>(
        Triple(instant, MILLI_OF_SECOND, instant.get(MILLI_OF_SECOND)),
        Triple(local, YEAR, Year.from(date).value),
        Triple(local, SECOND_OF_MINUTE, local.second),
        Triple(date, YEAR, Year.from(date).value)
      ).forEach { (subject, field, expected) ->
        test("maps $subject (${subject.javaClass.simpleName}) $field to $expected (Int)") {
          expectThat(subject).get(field).isEqualTo(expected)
        }
      }
    }

    context("getLong(TemporalField) mapping") {
      listOf<Triple<TemporalAccessor, TemporalField, Long>>(
        Triple(instant, MILLI_OF_SECOND, instant.getLong(MILLI_OF_SECOND)),
        Triple(local, YEAR, Year.from(date).value.toLong()),
        Triple(local, SECOND_OF_MINUTE, local.second.toLong()),
        Triple(date, YEAR, Year.from(date).value.toLong())
      ).forEach { (subject, field, expected) ->
        test("maps $subject (${subject.javaClass.simpleName}) $field to $expected (Long)") {
          expectThat(subject).getLong(field).isEqualTo(expected)
        }
      }
    }
  }
}
