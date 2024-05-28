package com.example.takeoutapplication.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(
    val phone: String,
    val password: String
)
