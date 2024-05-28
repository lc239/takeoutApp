package com.example.takeoutapplication.model.entity

import com.example.takeoutapplication.model.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Order(
    val orderId: String? = null,
    val userId: Long? = null,
    val restaurantId: Long? = null,
    val deliveryManId: Long? = null,
    var commentId: Long? = null,
    val menus: List<OrderedMenu> = listOf(),
    val packPrice: Int? = null,
    val deliveryPrice: Int? = null,
    val price: Long,
    val address: Address,
    @Serializable(with = InstantSerializer::class)
    val createTime: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val completeTime: Instant? = null,
    val taken: Boolean? = null
)