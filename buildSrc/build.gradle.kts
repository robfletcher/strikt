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
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.10.1")
  implementation("com.netflix.nebula:nebula-publishing-plugin:17.3.2")
}
