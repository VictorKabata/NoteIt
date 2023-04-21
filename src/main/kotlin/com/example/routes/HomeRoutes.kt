package com.example.routes

import com.example.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.homeRoutes() {

    get(Constants.HOME) {
        call.respondText("Hello World!")
    }

}