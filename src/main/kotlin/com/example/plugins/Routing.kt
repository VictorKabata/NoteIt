package com.example.plugins

import com.example.routes.authRoutes
import com.example.routes.homeRoutes
import com.example.routes.noteRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        // Home Route
        homeRoutes()

        // Registration and Login routes
        authRoutes()

        // Note routes
        noteRoutes()
    }
}

