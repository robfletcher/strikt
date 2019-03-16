package strikt.spring

import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import strikt.api.Assertion
import strikt.api.DescribeableBuilder

/**
 * Maps this assertion to an assertion on the content type of the servlet
 * response. If the subject has no `Content-Type` header the subject of the
 * mapped assertion is `null`.
 */
val Assertion.Builder<MockHttpServletResponse>.contentType: DescribeableBuilder<MediaType?>
  get() = get { contentType?.let(MediaType::parseMediaType) }

