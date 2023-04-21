package com.example.models

import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("email")
    var email: String,

    @SerialName("user_name")
    var userName: String,

    @SerialName("password")
    var hashPassword: String
):Principal
