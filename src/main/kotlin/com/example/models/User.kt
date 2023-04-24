package com.example.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @Expose @SerializedName("email") var email: String,

    @Expose @SerializedName("user_name") var userName: String,

    @Expose(serialize = false) var hashPassword: String
) : Principal
