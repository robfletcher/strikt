package strikt.protobuf

import com.google.protobuf.Any
import com.google.protobuf.InvalidProtocolBufferException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.opentest4j.AssertionFailedError
import rpg.Character
import rpg.Mace
import rpg.Role
import rpg.Sword
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.cause
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.internal.opentest4j.MappingFailed

@DisplayName("assertions on com.google.protobuf.Any")
class AnyAssertions {

  val subject: Character = Character
    .newBuilder()
    .apply {
      name = "Crom"
      role = Role.warrior
      weapon = Any.pack(Sword.newBuilder().setDamage("1d10").build())
    }
    .build()

  @Test
  fun `can assert that an Any field is empty`() {
    val subject = Character
      .newBuilder()
      .apply {
        name = "Crom"
        role = Role.warrior
      }
      .build()

    expectThat(subject)
      .get { weapon }
      .isEmpty()
  }

  @Test
  fun `assertion fails if an Any field is not empty`() {
    assertThrows<AssertionFailedError> {
      expectThat(subject)
        .get { weapon }
        .isEmpty()
    }
  }

  @Test
  fun `can assert that an Any field is a particular type`() {
    expectThat(subject)
      .get { weapon }
      .unpacksTo<Sword>()
  }

  @Test
  fun `assertion fails if an Any field is not the expected type`() {
    assertThrows<AssertionFailedError> {
      expectThat(subject)
        .get { weapon }
        .unpacksTo<Mace>()
    }
  }

  @Test
  fun `can unpack an any field and make assertions about it`() {
    expectThat(subject)
      .get { weapon }
      .unpack<Sword>()
      .get { damage }
      .isEqualTo("1d10")
  }

  @Test
  fun `trying to unpack a field to the wrong type throws an exception`() {
    expectThrows<MappingFailed> {
      expectThat(subject)
        .get { weapon }
        .unpack<Mace>()
    }
      .cause
      .isNotNull()
      .isA<InvalidProtocolBufferException>()
  }
}
