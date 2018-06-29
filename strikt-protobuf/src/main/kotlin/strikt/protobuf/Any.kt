package strikt.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.Internal.getDefaultInstance
import com.google.protobuf.Message
import strikt.api.Assertion

typealias AnyMessage = com.google.protobuf.Any

fun Assertion<AnyMessage>.isEmpty() {
  passesIf("is empty") {
    value == ByteString.EMPTY
  }
}

inline fun <reified T : Message> Assertion<AnyMessage>.unpacksTo(): Assertion<AnyMessage> =
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

inline fun <reified T : Message> Assertion<AnyMessage>.unpack(): Assertion<T> =
  map { unpack(T::class.java) }
