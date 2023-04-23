package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val message: String,
    val code: Int
)
