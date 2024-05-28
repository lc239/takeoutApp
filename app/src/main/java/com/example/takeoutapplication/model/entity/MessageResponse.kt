package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val code: Int,
    val message: String
)