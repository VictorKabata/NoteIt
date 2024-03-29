package com.example.plugins

import com.example.authentication.JwtService
import com.example.repository.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(userRepository: UserRepository = UserRepository()) {
    authentication {
        jwt("jwt") {
            verifier(JwtService.verifier)
            realm = "NoteIt Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = userRepository.getUser(email = email)
                user
            }
        }
    }
}
