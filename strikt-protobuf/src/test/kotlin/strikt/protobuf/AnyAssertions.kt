package strikt.protobuf

import com.google.protobuf.Any
import rpg.Character
import rpg.Role
import rpg.Sword
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect

@DisplayName("assertions on com.google.protobuf.Any")
class AnyAssertions {

  @Test
  fun `can assert that an Any field is empty`() {
    val subject = Character
      .newBuilder()
      .apply {
        name = "Crom"
        role = Role.warrior
      }

    expect(subject).map { weapon }.isEmpty()
  }

  @Test
  fun `can assert that an Any field is a particular type`() {
    val subject = Character
      .newBuilder()
      .apply {
        name = "Crom"
        role = Role.warrior
        weapon = Any.pack(Sword.newBuilder().setDamage("1d10").build())
      }

    expect(subject).map { weapon }.unpacksTo<Sword>()
  }
}
