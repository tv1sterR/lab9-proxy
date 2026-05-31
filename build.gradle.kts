plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.google.protobuf") version "0.9.4"
}

group = "ru.university"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.25.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("client.gui.App")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
}

// ВАЖНО: говорим Java, где лежат сгенерированные классы
sourceSets {
    main {
        java {
            srcDir("build/generated/source/proto/main/java")
        }
        proto {
            srcDir("src/main/proto")
        }
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

