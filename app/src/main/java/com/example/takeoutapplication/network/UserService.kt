package com.example.takeoutapplication.network

import com.example.takeoutapplication.model.entity.Address
import com.example.takeoutapplication.model.entity.CommonResponse
import com.example.takeoutapplication.model.entity.MessageResponse
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.model.request.LoginRequestBody
import com.example.takeoutapplication.model.entity.Tokens
import com.example.takeoutapplication.model.entity.User
import com.example.takeoutapplication.model.entity.UsernameRequest
import com.example.takeoutapplication.model.request.RegisterUserBody
import com.example.takeoutapplication.model.request.RestaurantCommentForm
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService{

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): CommonResponse<Tokens>

    @Headers("Content-Type: application/json")
    @POST("user/register")
    suspend fun register(
        @Body registerUserBody: RegisterUserBody
    ): CommonResponse<Tokens>

    @Headers("Content-Type: application/json")
    @GET("user/info")
    suspend fun getInfo(
        @Header("Authorization") auth: String
    ): CommonResponse<User>

    @Headers("Content-Type: application/json")
    @PUT("user/order/put/{restaurantId}")
    suspend fun putOrder(
        @Header("Authorization") auth: String,
        @Path("restaurantId") restaurantId: Long,
        @Body order: Order
    ): MessageResponse

    @Headers("Content-Type: application/json")
    @PATCH("user/address/add")
    suspend fun addAddress(
        @Header("Authorization") auth: String,
        @Body address: Address
    ): MessageResponse

    @GET("user/orders/{pageOffset}/{pageSize}")
    suspend fun getHistoryOrders(
        @Header("Authorization") auth: String,
        @Path("pageOffset") pageOffset: Int,
        @Path("pageSize") pageSize: Int
    ): CommonResponse<List<Order>>

    @Headers("Content-Type: application/json")
    @PUT("user/order/comment/{orderId}")
    suspend fun commentOrder(
        @Header("Authorization") auth: String,
        @Path("orderId") orderId: String,
        @Body comment: RestaurantCommentForm
    ): CommonResponse<RestaurantComment>

    @GET("user/orders/delivering")
    suspend fun getDeliveringOrders(
        @Header("Authorization") auth: String
    ): CommonResponse<List<Order>>

    @Multipart
    @PUT("user/upload/avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") auth: String,
        @Part avatar: MultipartBody.Part
    ): CommonResponse<String>

    @PUT("user/update/username")
    suspend fun updateUsername(
        @Header("Authorization") auth: String,
        @Body usernameRequest: UsernameRequest
    ): MessageResponse
}

object UserApi{
    val retrofitService: UserService by lazy {
        ApiBase.retrofit.create(UserService::class.java)
    }
}