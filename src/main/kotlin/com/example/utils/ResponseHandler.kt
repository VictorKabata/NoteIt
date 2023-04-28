package com.example.utils

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

object ResponseHandler {

    suspend inline fun <reified T : Any> ApplicationCall.successResponse(
        message: T,
        statusCode: HttpStatusCode = HttpStatusCode.OK
    ) {
        respond(status = statusCode, message = message)
    }

    suspend inline fun ApplicationCall.errorResponse(
        message: String,
        statusCode: HttpStatusCode = HttpStatusCode.InternalServerError
    ) {
        val errorBody = mapOf(
            "code" to statusCode.value,
            "message" to message
        )

        respond(status = statusCode, message = errorBody)
    }
}
