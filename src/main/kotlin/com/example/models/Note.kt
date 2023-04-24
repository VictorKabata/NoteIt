package com.example.models

import com.google.gson.annotations.Expose
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @Expose val id: Int,
    @Expose val title: String,
    @Expose val description: String,
    @Expose val createdAt: String,
    @Expose val updatedAt: String? = null
)
