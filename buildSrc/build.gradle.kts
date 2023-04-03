plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.8.10")
  implementation("com.netflix.nebula:nebula-publishing-plugin:20.2.0")
}
