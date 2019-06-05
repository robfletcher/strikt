plugins {
  `java-library`
  id("nebula.kotlin")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api(project(":strikt-core"))
  implementation("com.squareup.okhttp3:mockwebserver:3.14.2")
}
