package com.example.takeoutapplication.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserBody(
    val username: String,
    val phone: String,
    val password: String,
    val isSeller: Boolean,
    val isDeliveryMan: Boolean
)