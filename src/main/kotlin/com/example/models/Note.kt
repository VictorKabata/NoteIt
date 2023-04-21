package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String? = null
)
