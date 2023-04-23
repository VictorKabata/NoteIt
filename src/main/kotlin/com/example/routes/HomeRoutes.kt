package com.example.routes

import com.example.utils.Constants
import com.example.utils.ResponseHandler.successResponse
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Route.homeRoutes() {

    get(Constants.HOME) {
        call.successResponse(message = "Hello, World!")
    }

}