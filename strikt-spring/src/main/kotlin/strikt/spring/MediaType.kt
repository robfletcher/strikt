package strikt.spring

import org.springframework.http.MediaType
import strikt.api.Assertion

/**
 * Asserts that the subject is compatible with [expected].
 *
 * For example, a subject of `application/json;charset=UTF-8` is _compatible
 * with_ the media type `application/json`, but not with the media type
 * `application/x-yaml`.
 */
@Suppress("UNCHECKED_CAST")
infix fun <T : MediaType?> Assertion.Builder<T>.isCompatibleWith(expected: MediaType): Assertion.Builder<MediaType> =
  assertThat("is compatible with $expected") {
    it?.isCompatibleWith(expected) ?: false
  } as Assertion.Builder<MediaType>
