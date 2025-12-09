plugins {
    id("buildlogic.kotlin-application-conventions")
    id("com.avast.gradle.docker-compose") version "0.17.0"
    id("com.google.protobuf") version "0.9.5"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":utilities"))

    implementation("mysql:mysql-connector-java:8.0.25")

    implementation("io.netty:netty-all:4.1.74.Final")

    implementation("io.insert-koin:koin-core:3.2.1")

    implementation("commons-beanutils:commons-beanutils:1.9.4")

    implementation("com.google.protobuf:protobuf-java:4.33.1")
    implementation("com.google.protobuf:protobuf-java-util:4.33.1")

    implementation("io.grpc:grpc-stub:1.77.0")
    implementation("io.grpc:grpc-protobuf:1.77.0")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")
    runtimeOnly("io.grpc:grpc-netty-shaded:1.77.0")

    testImplementation(dependencies.project(mapOf("path" to ":core", "configuration" to "testOutput")))
    testImplementation("io.insert-koin:koin-test:3.2.1")
}

application {
    mainClass = "fr.rob.game.Main"
}

dockerCompose {
    dockerComposeWorkingDirectory.set(project.rootProject.projectDir)
    setProjectName("coder")
    removeVolumes.set(false)
    useDockerComposeV2.set(true)
    useComposeFiles.set(listOf("compose.yaml"))
    isRequiredBy(tasks.run)
}

dockerCompose.nested("test").apply {
    dockerComposeWorkingDirectory.set(project.rootProject.projectDir)
    setProjectName("coder-test")
    useDockerComposeV2.set(true)
    useComposeFiles.set(listOf("compose.test.yaml"))
    isRequiredBy(tasks.test)
}

sourceSets {
    main {
        proto {
            srcDir("${rootProject.projectDir}/proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3:linux-x86_64@exe"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.77.0:linux-x86_64@exe"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}
