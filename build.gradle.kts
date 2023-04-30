plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.ktLint)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.serialization.gson)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.logging)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.requestValidation)
    implementation(libs.ktor.server.monitoring)
    implementation(libs.ktor.server.swagger)

    implementation(libs.prometheus)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgres)
    implementation(libs.hikari)

    // implementation(libs.logback)

    implementation(libs.config4k)

    implementation(libs.kotlinx.datetime)
    implementation("io.ktor:ktor-server-cors-jvm:2.2.4")

    testImplementation(libs.ktor.server.test)
    testImplementation(libs.kotlin.test.junit)
}

ktor {
    docker {
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
        localImageName.set("app")
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    filter {
        enableExperimentalRules.set(true)
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
        include("**/kotlin/**")
    }
}
