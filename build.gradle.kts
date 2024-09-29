plugins {
    kotlin("jvm") version "2.0.20"
}

group = "uk.co.kiteframe.tictactoe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.1")
    implementation("io.arrow-kt:arrow-core:1.2.4")
}

tasks.test {
    useJUnitPlatform()
}