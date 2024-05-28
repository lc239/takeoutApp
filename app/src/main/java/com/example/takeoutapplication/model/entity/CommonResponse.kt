package com.example.takeoutapplication.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null
)