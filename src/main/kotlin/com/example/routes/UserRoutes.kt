package com.example.routes

import com.example.authentication.JwtService.generateToken
import com.example.authentication.JwtService.hash
import com.example.models.LoginRequest
import com.example.models.User
import com.example.models.UserToken
import com.example.repository.UserRepository
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**Auth routes for user registration and login*/
fun Route.authRoutes(userRepository: UserRepository = UserRepository()) {

    // Register Route
    post(Constants.REGISTER) {
        val userResponse = try {
            call.receive<User>().apply {
                this.hashPassword = this.hashPassword.hash()
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            return@post
        }

        try {
            userRepository.createUser(user = userResponse)
            call.respond(HttpStatusCode.OK, userResponse)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.localizedMessage)
        }
    }

    // Login Route
    post(Constants.LOGIN) {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            return@post
        }

        try {
            val user = userRepository.findUserByEmail(loginRequest.email)

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else {

                if (user.hashPassword == loginRequest.hashPassword.hash()) {
                    val userToken = UserToken(token = user.generateToken())

                    call.respond(HttpStatusCode.OK, userToken)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid password")
                }

            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.localizedMessage)
        }
    }

}