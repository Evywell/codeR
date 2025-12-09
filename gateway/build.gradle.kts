plugins {
    id("buildlogic.kotlin-application-conventions")
    idea
    id("com.google.protobuf") version "0.9.5"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":utilities"))

    implementation("io.netty:netty-all:4.1.74.Final")

    implementation("com.google.protobuf:protobuf-java:4.33.1")
    implementation("com.google.protobuf:protobuf-java-util:4.33.1")

    implementation("io.grpc:grpc-stub:1.77.0")
    implementation("io.grpc:grpc-protobuf:1.77.0")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")
    runtimeOnly("io.grpc:grpc-netty-shaded:1.77.0")
}

sourceSets {
    main {
        proto {
            srcDir("${rootProject.projectDir}/proto")
        }
    }
}

application {
    mainClass = "fr.rob.gateway.Main"
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
