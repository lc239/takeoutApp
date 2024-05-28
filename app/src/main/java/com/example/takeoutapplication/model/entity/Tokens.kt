package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Tokens(
    val token: String,
    val refreshToken: String
)