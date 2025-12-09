plugins {
    id("buildlogic.kotlin-library-conventions")
    id("com.google.protobuf") version "0.9.5"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")

    implementation("io.netty:netty-all:4.2.7.Final")
    // Used for Netty's SSL testing utilities (certificates generation)
    implementation("io.netty:netty-pkitesting:4.2.7.Final")

    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")

    implementation("org.apache.commons:commons-configuration2:2.7")

    implementation("com.google.protobuf:protobuf-java:4.33.1")
    implementation("com.google.protobuf:protobuf-java-util:4.33.1")
    implementation(project(":utilities"))

    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}

tasks.named<Test>("test") {
    failOnNoDiscoveredTests.set(false)
}

// Expose test classes for other modules (such as BaseTest)
val testJar by tasks.registering(Jar::class) {
    archiveClassifier.set("test")
    from(sourceSets["test"].output)
}

configurations {
    create("testOutput") {
        isCanBeConsumed = true
        isCanBeResolved = false
    }
}

artifacts {
    add("testOutput", testJar)
}
