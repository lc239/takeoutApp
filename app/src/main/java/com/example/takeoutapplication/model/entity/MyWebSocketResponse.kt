package com.example.takeoutapplication.model.entity

import com.example.takeoutapplication.model.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime

@Serializable
data class MyWebSocketResponse(
    val type: Int,
    @Serializable(with = InstantSerializer::class)
    val time: Instant,
    val data: String?
)
