package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantPreview(
    val id: Long,
    val name: String,
    val saleNum: Long,
    val rate: Long,
    val rateCount: Long,
    val imageFilename: String,
    val deliveryPrice: Long
)