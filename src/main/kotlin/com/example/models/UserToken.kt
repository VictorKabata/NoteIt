package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
    var token: String
)
