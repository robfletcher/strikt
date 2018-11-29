plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  jcenter()
  gradlePluginPortal()
  maven(url = "https://kotlin.bintray.com/kotlinx")
}

dependencies {
  compile("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
  compile("com.netflix.nebula:nebula-publishing-plugin:9.0.1")
  compile("com.netflix.nebula:nebula-bintray-plugin:4.0.1")
  compile("com.ferranpons:twitter-gradle-plugin:1.1.0")
}
