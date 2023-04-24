package com.example.models

import com.google.gson.annotations.Expose
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Expose var email: String,

    @Expose var userName: String,

    @Expose(serialize = false) var hashPassword: String
) : Principal
