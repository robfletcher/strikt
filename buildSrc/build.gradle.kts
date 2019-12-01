plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  jcenter()
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.10.0")
  implementation("com.netflix.nebula:nebula-publishing-plugin:14.1.1")
  implementation("com.netflix.nebula:nebula-bintray-plugin:6.0.6")
}
