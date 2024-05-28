package com.example.takeoutapplication.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantRegister(
    val name: String,
    val introduction: String
)