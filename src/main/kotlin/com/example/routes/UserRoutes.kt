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
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**Auth routes for user registration and login*/
fun Route.authRoutes(userRepository: UserRepository = UserRepository()) {

    // Register Route
    post(Constants.REGISTER) {
        val userResponse: User? = try {
            call.receive<User>().apply {
                this.hashPassword = this.hashPassword.hash()
            }
        } catch (e: Exception) {
            call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = e.localizedMessage)
            return@post
        }

        try {
            if (userResponse == null) {
                call.errorResponse(statusCode = HttpStatusCode.BadRequest, message = "Missing user request body")
            } else {
                userRepository.createUser(user = userResponse)
                call.successResponse(statusCode = HttpStatusCode.Created, data = userResponse)
            }
        } catch (e: Exception) {
            call.errorResponse(statusCode = HttpStatusCode.Conflict, message = e.localizedMessage)
        }
    }

    // Login Route
    post(Constants.LOGIN) {
        val emailQueryParam = call.request.queryParameters["email"] ?: return@post call.respond(
            HttpStatusCode.BadRequest,
            "Missing user email"
        )

        val passwordQueryParam = call.request.queryParameters["password"] ?: return@post call.respond(
            HttpStatusCode.BadRequest,
            "Missing user password"
        )

        try {
            val user = userRepository.getUser(emailQueryParam)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {

                if (user.hashPassword == passwordQueryParam.hash()) {

                    val userToken = mapOf("token" to user.generateToken())

                    call.respond(HttpStatusCode.OK, userToken)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid password")
                }

            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.localizedMessage)
        }
    }

    // Get User Route
    get(Constants.GET_USER) {
        val emailQueryParam = call.request.queryParameters["email"] ?: return@get call.respond(
            HttpStatusCode.BadRequest,
            "Missing user email"
        )

        try {
            val user = userRepository.getUser(email = emailQueryParam)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                call.respond(HttpStatusCode.OK, user)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.localizedMessage)
        }
    }

    authenticate("jwt") {
        // Update User Route
        put(Constants.UPDATE_USER) {
            val email =
                call.principal<User>()?.email ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid token")

            val user = try {
                call.receive<User>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                return@put
            }

            try {
                userRepository.updateUser(user = user, email = email)

                val updatedUser = userRepository.getUser(email = email)
                if (updatedUser == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    call.respond(HttpStatusCode.OK, updatedUser)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }

        // Delete User Route
        delete(Constants.DELETE_USER) {
            val email =
                call.principal<User>()?.email ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid token")

            try {
                val user = userRepository.getUser(email = email)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                } else {
                    userRepository.deleteUser(email = email)
                    call.respond(HttpStatusCode.Accepted, "User deleted")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage)
            }
        }
    }

}