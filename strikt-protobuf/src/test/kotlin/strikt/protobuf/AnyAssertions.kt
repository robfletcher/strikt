package strikt.protobuf

import com.google.protobuf.Any
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import rpg.Character
import rpg.Role
import rpg.Sword
import strikt.api.expectThat

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
      .build()

    expectThat(subject).traverse(Character::getWeapon).isEmpty()
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
      .build()

    expectThat(subject).traverse(Character::getWeapon).unpacksTo<Sword>()
  }
}
