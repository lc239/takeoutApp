package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val username: String,
    val phone: String,
    val isSeller: Boolean,
    val isDeliveryMan: Boolean,
    val avatarFilename: String,
    val addresses: List<Address>
)

@Serializable
data class UsernameRequest(
    val username: String
)