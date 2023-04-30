package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.configureDocumentation() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
}
