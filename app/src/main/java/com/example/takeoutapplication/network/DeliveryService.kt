package com.example.takeoutapplication.network

import com.example.takeoutapplication.model.entity.Address
import com.example.takeoutapplication.model.entity.CommonResponse
import com.example.takeoutapplication.model.entity.DeliveryMan
import com.example.takeoutapplication.model.entity.MessageResponse
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.model.request.LoginRequestBody
import com.example.takeoutapplication.model.entity.Tokens
import com.example.takeoutapplication.model.entity.User
import com.example.takeoutapplication.model.request.RegisterUserBody
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface DeliveryService{
    @POST("delivery/register")
    suspend fun register(
        @Header("Authorization") auth: String
    ): CommonResponse<DeliveryMan>

    @GET("delivery/order/take/{indexStart}/{indexEnd}")
    suspend fun getOrders(
        @Header("Authorization") auth: String,
        @Path("indexStart") indexStart: Int,
        @Path("indexEnd") indexEnd: Int
    ): CommonResponse<List<OrderDeliveryView>>

    @GET("delivery/info")
    suspend fun getInfo(
        @Header("Authorization") auth: String
    ): CommonResponse<DeliveryMan>

    @PATCH("delivery/order/take/{orderId}")
    suspend fun takeOrder(
        @Header("Authorization") auth: String,
        @Path("orderId") orderId: String
    ): CommonResponse<Order>

    @PATCH("delivery/order/complete/{orderId}")
    suspend fun completeOrder(
        @Header("Authorization") auth: String,
        @Path("orderId") orderId: String
    ): CommonResponse<DeliveryMan>

    @GET("delivery/order/delivering")
    suspend fun getDeliveringOrders(
        @Header("Authorization") auth: String
    ): CommonResponse<List<Order>>
}

object DeliveryApi{
    val retrofitService: DeliveryService by lazy {
        ApiBase.retrofit.create(DeliveryService::class.java)
    }
}