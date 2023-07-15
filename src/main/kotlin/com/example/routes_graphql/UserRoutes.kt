package com.example.routes_graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.example.authentication.JwtService.generateToken
import com.example.authentication.JwtService.hash
import com.example.models.User
import com.example.repository.UserRepository

fun SchemaBuilder.userGraphQLRoutes(userRepository: UserRepository = UserRepository()) {

    query("user") {
        description = "Get user info"
        resolver { email: String -> userRepository.getUser(email = email) }
    }

    mutation("register_user") {
        description = "Register a new user"
        resolver { user: User -> userRepository.createUser(user = user) }
    }

    query("login") {
        description = "Generate a user auth token"
        resolver { email: String, password: String ->
            val user = userRepository.getUser(email = email)

            return@resolver if (user != null && user.hashPassword == password.hash()) {
                user.generateToken()
            } else null
        }
    }

    mutation("update_user") {
        description = "Update existing user"
        resolver { email: String, user: User ->
            userRepository.updateUser(user = user, email = email)
        }
    }

    mutation("delete_user") {
        description = "Delete existing user"
        resolver { email: String ->
            userRepository.deleteUser(email = email)
        }
    }

}
