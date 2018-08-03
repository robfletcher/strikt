package strikt.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.Internal.getDefaultInstance
import com.google.protobuf.Message
import strikt.api.Asserter

/**
 * Asserts that a `com.google.protobuf.Any` is empty and does not contain an
 * object of any kind.
 */
fun Asserter<com.google.protobuf.Any>.isEmpty() {
  passesIf("is empty") {
    value == ByteString.EMPTY
  }
}

/**
 * Asserts that the subject is a message of type [T].
 *
 * @see com.google.protobuf.Any.is
 */
inline fun <reified T : Message> Asserter<com.google.protobuf.Any>.unpacksTo(): Asserter<com.google.protobuf.Any> =
  assert(
    "unpacks to %s",
    getDefaultInstance(T::class.java).descriptorForType.fullName
  ) {
    if (subject.`is`(T::class.java)) {
      pass()
    } else {
      fail(actual = subject.typeUrl)
    }
  }

/**
 * Maps an assertion on `com.google.protobuf.Any` to an assertion on an unpacked
 * message of type [T].
 *
 * @see com.google.protobuf.Any.unpack
 */
inline fun <reified T : Message> Asserter<com.google.protobuf.Any>.unpack(): Asserter<T> =
  map { unpack(T::class.java) }
