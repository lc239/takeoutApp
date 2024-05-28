package com.example.takeoutapplication.model.entity

import com.example.takeoutapplication.model.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class RestaurantComment(
    val id: Long? = null,
    val userId: Long? = null,
    val username: String? = null,
    val restaurantId: Long? = null,
    val content: String = "",
    val rate: Int = 5,
    val images: List<String> = listOf(),
    @Serializable(InstantSerializer::class)
    val createTime: Instant? = null
)