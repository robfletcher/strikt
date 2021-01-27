plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api("org.opentest4j:opentest4j:+")

  implementation("com.christophsturm:filepeek:+")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

  testImplementation("dev.minutest:minutest:+")
}

val failfastTests =
  task("failfastTests", JavaExec::class) {
    main = "strikt.FailfastKt"
    classpath = sourceSets["test"].runtimeClasspath
  }

tasks.check { dependsOn(failfastTests) }
