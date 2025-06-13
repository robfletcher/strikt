plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
  implementation(libs.dokka)
  implementation(libs.nebula.publishing)
}
