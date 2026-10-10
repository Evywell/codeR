plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.25")
}

application {
    mainClass = "fr.rob.e2e.MainKt"
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
    dependsOn(":servers:game:installDist")
}
