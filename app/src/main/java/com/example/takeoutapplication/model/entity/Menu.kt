package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val name: String,
    val description: String,
    val price: Long,
    val imageFilename: String
)