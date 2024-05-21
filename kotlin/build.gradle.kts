plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.github.jntakpe"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}
