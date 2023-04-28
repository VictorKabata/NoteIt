package com.example.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @Expose @SerializedName("id")
    val id: String,
    @Expose @SerializedName("title")
    val title: String,
    @Expose @SerializedName("description")
    val description: String,
    @Expose @SerializedName("created_at")
    val createdAt: String,
    @Expose @SerializedName("updated_at")
    val updatedAt: String? = null
)
