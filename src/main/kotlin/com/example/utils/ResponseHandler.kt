package com.example.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

object ResponseHandler {

    suspend inline fun <reified T : Any> ApplicationCall.successResponse(
        statusCode: HttpStatusCode = HttpStatusCode.OK,
        message: T
    ) {
        respond(status = statusCode, message = message)
    }

    suspend inline fun ApplicationCall.errorResponse(
        statusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
        message: String
    ) {
        respond(status = statusCode, message = message)
    }

}