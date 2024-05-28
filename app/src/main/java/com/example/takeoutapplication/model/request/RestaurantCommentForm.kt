package com.example.takeoutapplication.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantCommentForm(
    val rate: Int,
    val content: String
)