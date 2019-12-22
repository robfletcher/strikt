package strikt.spring

import javax.servlet.ServletResponse
import org.springframework.http.MediaType
import strikt.api.Assertion

/**
 * Maps this assertion to an assertion on the content type of the servlet
 * response. If the subject has no `Content-Type` header the subject of the
 * mapped assertion is `null`.
 */
val <T : ServletResponse> Assertion.Builder<T>.contentType: Assertion.Builder<MediaType?>
  get() = get { contentType?.let(MediaType::parseMediaType) }

/**
 * Asserts that the content type of the subject is compatible with [expected].
 *
 * @see MediaType.isCompatibleWith
 */
infix fun <T : ServletResponse> Assertion.Builder<T>.contentTypeIsCompatibleWith(
  expected: MediaType
): Assertion.Builder<T> =
  assertThat("content type is compatible with $expected") {
    it.contentType.let(MediaType::parseMediaType).isCompatibleWith(expected)
  }
