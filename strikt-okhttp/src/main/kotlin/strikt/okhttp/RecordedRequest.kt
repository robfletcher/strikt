package strikt.okhttp

import okhttp3.mockwebserver.RecordedRequest
import strikt.api.Assertion

/**
 * Maps this assertion to an assertion on the named header.
 *
 * @return an assertion on the named header's value if it exists. If the request
 * did not contain that header the subject will be `null`.
 */
fun Assertion.Builder<RecordedRequest>.getHeader(name: String): Assertion.Builder<String?> =
  get("$name header") { getHeader(name) }
