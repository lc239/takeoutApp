package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id: Long,
    val name: String,
    val introduction: String,
    val saleNum: Long,
    val rate: Long,
    val rateCount: Long,
    val imageFilename: String,
    val categories: List<Category>,
    val deliveryPrice: Long
)