package com.example.routes

import com.example.authentication.JwtService.generateToken
import com.example.authentication.JwtService.hash
import com.example.models.User
import com.example.repository.UserRepository
import com.example.utils.Constants
import com.example.utils.ResponseHandler.errorResponse
import com.example.utils.ResponseHandler.successResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

/**Auth routes for user registration and login*/
fun Route.authRoutes(userRepository: UserRepository = UserRepository()) = route(Constants.USERS) {
    // Get User Route
    get {
        val emailQueryParam = call.request.queryParameters["email"]
            ?: return@get call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = "Missing user email")

        try {
            val user = userRepository.getUser(email = emailQueryParam)

            if (user == null) {
                call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "User not found")
            } else {
                call.successResponse(message = user)
            }
        } catch (e: Exception) {
            call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
        }
    }

    // Register Route
    post(Constants.REGISTER) {
        val userRequestBody: User? = try {
            call.receive<User>().apply {
                this.hashPassword = this.hashPassword.hash()
            }
        } catch (e: Exception) {
            call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = e.localizedMessage)
            return@post
        }

        try {
            if (userRequestBody == null) {
                call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = "Missing user request body")
            } else {


                userRepository.createUser(user = userRequestBody)
                call.successResponse(statusCode = HttpStatusCode.Created, message = userRequestBody)
            }
        } catch (e: Exception) {
            call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
        }
    }

    // Login Route
    post(Constants.LOGIN) {
        val emailQueryParam = call.request.queryParameters["email"]
            ?: return@post call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Missing user email"
            )

        val passwordQueryParam = call.request.queryParameters["password"]
            ?: return@post call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Missing user password"
            )

        try {
            val user = userRepository.getUser(emailQueryParam)

            if (user == null) {
                call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "User not found")
            } else {

                if (user.hashPassword == passwordQueryParam.hash()) {

                    val userToken = mapOf("token" to user.generateToken())

                    call.successResponse(message = userToken)
                } else {
                    call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = "Invalid password")
                }

            }

        } catch (e: Exception) {
            call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
        }
    }

    authenticate("jwt") {
        // Update User Route
        put(Constants.UPDATE_USER) {
            val email = call.principal<User>()?.email ?: return@put call.errorResponse(
                statusCode = HttpStatusCode.BadRequest,
                message = "Invalid token"
            )

            val user = try {
                call.receive<User>()
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = e.localizedMessage)
                return@put
            }

            try {
                userRepository.updateUser(user = user, email = email)

                val updatedUser = userRepository.getUser(email = email)
                if (updatedUser == null) {
                    call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "User not found")
                } else {
                    call.successResponse(message = updatedUser)
                }
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }

        // Delete User Route
        delete(Constants.DELETE_USER) {
            val email = call.principal<User>()?.email
                ?: return@delete call.errorResponse(
                    statusCode = HttpStatusCode.BadRequest,
                    message = "Invalid token"
                )

            try {
                val user = userRepository.getUser(email = email)
                if (user == null) {
                    call.errorResponse(statusCode = HttpStatusCode.NotFound, message = "User not found")
                } else {
                    userRepository.deleteUser(email = email)
                    call.successResponse(
                        statusCode = HttpStatusCode.Accepted,
                        message = mapOf("message" to "User successfully deleted")
                    )
                }
            } catch (e: Exception) {
                call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
            }
        }
    }
}