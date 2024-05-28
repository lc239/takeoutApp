package com.example.takeoutapplication.model.entity

import com.example.takeoutapplication.model.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class OrderDeliveryView(
    val orderId: String,
    val address: Address,
    @Serializable(with = InstantSerializer::class)
    val createTime: Instant
)
