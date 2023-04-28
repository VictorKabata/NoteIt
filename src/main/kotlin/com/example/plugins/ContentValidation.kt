package com.example.plugins

import com.example.models.Note
import com.example.models.User
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureContentValidation() {
    install(RequestValidation) {
        validate<User> {
            when {
                it.email.isEmpty() -> ValidationResult.Invalid("Missing email address")
                !it.email.isValidEmail() -> ValidationResult.Invalid("Invalid email address")
                it.userName.isEmpty() -> ValidationResult.Invalid("Missing user name")
                it.hashPassword.isEmpty() -> ValidationResult.Invalid("Missing password")
                else -> ValidationResult.Valid
            }
        }

        validate<Note> {
            if (it.title.isEmpty()) ValidationResult.Invalid("Missing note title")
            else if (it.description.isEmpty()) ValidationResult.Invalid("Missing note title")
            else ValidationResult.Valid
        }
    }
}

private fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$\n")
    return this.matches(emailRegex)
}
