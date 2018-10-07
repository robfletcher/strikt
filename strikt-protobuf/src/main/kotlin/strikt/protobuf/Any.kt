package strikt.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.Internal.getDefaultInstance
import com.google.protobuf.Message
import strikt.api.Assertion.Builder

/**
 * Asserts that a `com.google.protobuf.Any` is empty and does not contain an
 * object of any kind.
 */
fun Builder<com.google.protobuf.Any>.isEmpty() {
  assertThat("is empty") {
    it.value == ByteString.EMPTY
  }
}

/**
 * Asserts that the subject is a message of type [T].
 *
 * @see com.google.protobuf.Any.is
 */
inline fun <reified T : Message> Builder<com.google.protobuf.Any>.unpacksTo(): Builder<com.google.protobuf.Any> {
  val expected = getDefaultInstance(T::class.java).descriptorForType.fullName
  return assert("unpacks to %s", expected) {
    if (it.`is`(T::class.java)) {
      pass()
    } else {
      fail(actual = it.descriptorForType.fullName)
    }
  }
}

/**
 * Maps an assertion on `com.google.protobuf.Any` to an assertion on an unpacked
 * message of type [T].
 *
 * @see com.google.protobuf.Any.unpack
 */
inline fun <reified T : Message> Builder<com.google.protobuf.Any>.unpack(): Builder<T> =
  get { unpack(T::class.java) }
