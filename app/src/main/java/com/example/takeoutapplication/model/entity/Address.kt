package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val name: String,
    val phone: String,
    val address: String
)