package com.example.takeoutapplication.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MenuRequestBody(
    val name: String,
    val description: String,
    val price: Long
)