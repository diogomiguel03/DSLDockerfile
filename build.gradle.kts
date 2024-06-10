plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javaFXVersion = "22.0.1"
val javaFXSDK = "/Users/diogosilva/javafx-sdk-22.0.1/lib" // Change this to the correct path of your JavaFX SDK

dependencies {
    testImplementation(kotlin("test"))

    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.openjfx:javafx-base:$javaFXVersion:mac-aarch64")
    implementation("org.openjfx:javafx-controls:$javaFXVersion:mac-aarch64")
    implementation("org.openjfx:javafx-fxml:$javaFXVersion:mac-aarch64")
    implementation("org.openjfx:javafx-graphics:$javaFXVersion:mac-aarch64")
    implementation("org.openjfx:javafx-web:$javaFXVersion:mac-aarch64")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("com.typesafe:config:1.4.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("org.slf4j:slf4j-api:1.7.32")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("org.example.MainKt")

    applicationDefaultJvmArgs = listOf(
        "--module-path", javaFXSDK,
        "--add-modules", "javafx.controls,javafx.fxml,javafx.web"
    )
}
