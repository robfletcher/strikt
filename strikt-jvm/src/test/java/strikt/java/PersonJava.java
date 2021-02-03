package strikt.java;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class PersonJava {
  private final UUID id;
  private final String name;
  private final LocalDate dateOfBirth;
  private final byte[] image;

  public PersonJava(UUID id, String name, LocalDate dateOfBirth, byte[] image) {
    this.id = id;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.image = image;
  }

  public PersonJava(String name, LocalDate dateOfBirth, byte[] image) {
    this(UUID.randomUUID(), name, dateOfBirth, image);
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public byte[] getImage() {
    return Arrays.copyOf(image, image.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PersonJava that = (PersonJava) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Person(" + name + ")";
  }
}
