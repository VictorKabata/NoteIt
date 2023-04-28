package com.example.routes

import com.example.utils.Constants
import com.example.utils.ResponseHandler.successResponse
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.homeRoutes() = route(Constants.HOME) {
    get {
        call.successResponse(message = mapOf("message" to "Hello, World!"))
    }
}
