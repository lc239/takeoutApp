package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val name: String,
    val menus: List<Menu>
)