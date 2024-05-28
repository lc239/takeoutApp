package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantSearchView(
    val id: Long,
    val name: String,
    val rate: Long,
    val rateCount: Long
)