package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class OrderedMenu(
    val categoryIndex: Int = -1,
    val name: String,
    val num: Int,
    val price: Long
)
