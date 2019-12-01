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
  compile("org.jetbrains.dokka:dokka-gradle-plugin:0.10.0")
  compile("com.netflix.nebula:nebula-publishing-plugin:14.1.1")
  compile("com.netflix.nebula:nebula-bintray-plugin:6.0.6")
}
