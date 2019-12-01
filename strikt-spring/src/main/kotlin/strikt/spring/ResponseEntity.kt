package strikt.spring

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import strikt.api.Assertion

/**
 * Asserts that the status code is a 1xx Information response.
 */
fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs1xxInformational(): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is 1xx Informational") {
    if (it.statusCode.is1xxInformational) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }

/**
 * Asserts that the status code is a 2xx Success response.
 */
fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs2xxSuccess(): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is 2xx success") {
    if (it.statusCode.is2xxSuccessful) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }

/**
 * Asserts that the status code is a 3xx Redirect response.
 */
fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs3xxRedirection(): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is 3xx Redirection") {
    if (it.statusCode.is3xxRedirection) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }

/**
 * Asserts that the status code is a 4xx Client Error response.
 */
fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs4xxClientError(): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is 4xx Client Error") {
    if (it.statusCode.is4xxClientError) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }

/**
 * Asserts that the status code is a 5xx Server Error response.
 */
fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs5xxServerError(): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is 5xx Client Error") {
    if (it.statusCode.is5xxServerError) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }

/**
 * Asserts that the status code is equal to [expected].
 */
infix fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs(expected: Int): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is $expected") {
    if (it.statusCode.value() == expected) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }

/**
 * Asserts that the status code is equal to [expected].
 */
infix fun <T> Assertion.Builder<ResponseEntity<T>>.statusCodeIs(expected: HttpStatus): Assertion.Builder<ResponseEntity<T>> =
  assert("Status code is $expected") {
    if (it.statusCode == expected) {
      pass()
    } else {
      fail("status code is ${it.statusCode.value()}")
    }
  }
