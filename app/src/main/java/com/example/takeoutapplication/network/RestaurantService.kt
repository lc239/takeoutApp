package com.example.takeoutapplication.network

import com.example.takeoutapplication.model.entity.CommonResponse
import com.example.takeoutapplication.model.entity.Menu
import com.example.takeoutapplication.model.entity.MessageResponse
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.Restaurant
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.model.entity.RestaurantPreview
import com.example.takeoutapplication.model.entity.RestaurantSearchView
import com.example.takeoutapplication.model.request.MenuRequestBody
import com.example.takeoutapplication.model.request.RestaurantRegister
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface RestaurantService{

    @Headers("Content-Type: application/json")
    @POST("restaurant/register")
    suspend fun register(
        @Header("Authorization") auth: String,
        @Body restaurant: RestaurantRegister
    ): CommonResponse<Restaurant>

    @Headers("Content-Type: application/json")
    @GET("restaurant/info")
    suspend fun getInfo(
        @Header("Authorization") auth: String
    ): CommonResponse<Restaurant>

    @Headers("Content-Type: application/json")
    @GET("restaurant/info/{restaurantId}")
    suspend fun getByRestaurantId(
        @Header("Authorization") auth: String,
        @Path("restaurantId") id: Long
    ): CommonResponse<Restaurant>

    @Headers("Content-Type: application/json")
    @GET("restaurant/search/{size}/{prefix}")
    suspend fun searchRestaurants(
        @Header("Authorization") auth: String,
        @Path("size") size: Int,
        @Path("prefix") prefix: String
    ): CommonResponse<List<RestaurantSearchView>>

    @Headers("Content-Type: application/json")
    @PATCH("restaurant/category/add/{name}")
    suspend fun addCategory(
        @Header("Authorization") auth: String,
        @Path("name") name: String
    ): MessageResponse

    @Headers("Content-Type: application/json")
    @PATCH("restaurant/category/delete/{index}")
    suspend fun deleteCategory(
        @Header("Authorization") auth: String,
        @Path("index") index: Int
    ): MessageResponse

    @Headers("Content-Type: application/json")
    @PATCH("restaurant/menu/add/{index}")
    suspend fun addMenu(
        @Header("Authorization") auth: String,
        @Path("index") index: Int,
        @Body menu: MenuRequestBody
    ): CommonResponse<Menu>

    @Headers("Content-Type: application/json")
    @PATCH("restaurant/menu/update/{categoryIndex}/{menuIndex}")
    suspend fun updateMenu(
        @Header("Authorization") auth: String,
        @Path("categoryIndex") categoryIndex: Int,
        @Path("menuIndex") menuIndex: Int,
        @Body menu: Menu
    ): MessageResponse

    @Headers("Content-Type: application/json")
    @PATCH("restaurant/menu/delete/{categoryIndex}/{menuIndex}")
    suspend fun deleteMenu(
        @Header("Authorization") auth: String,
        @Path("categoryIndex") categoryIndex: Int,
        @Path("menuIndex") menuIndex: Int
    ): MessageResponse

    @Multipart
    @PUT("restaurant/menu/upload/image/{categoryIndex}/{menuIndex}")
    suspend fun updateMenuImage(
        @Header("Authorization") auth: String,
        @Path("categoryIndex") categoryIndex: Int,
        @Path("menuIndex") menuIndex: Int,
        @Part image: MultipartBody.Part
    ): CommonResponse<String>

    @GET("restaurant/info/{pageOffset}/{pageSize}")
    suspend fun getRestaurantsPage(
        @Header("Authorization") auth: String,
        @Path("pageOffset") pageOffset: Int,
        @Path("pageSize") pageSize: Int
    ): CommonResponse<List<RestaurantPreview>>

    @GET("restaurant/order/take/{orderId}")
    suspend fun getNotTakenOrder(
        @Header("Authorization") auth: String,
        @Path("orderId") orderId: String
    ): CommonResponse<Order>

    @PUT("restaurant/order/take/{orderId}")
    suspend fun takeOrder(
        @Header("Authorization") auth: String,
        @Path("orderId") orderId: String
    ): MessageResponse

    @DELETE("restaurant/order/delete/{orderId}")
    suspend fun rejectOrder(
        @Header("Authorization") auth: String,
        @Path("orderId") orderId: String
    ): MessageResponse

    @GET("restaurant/comment/{restaurantId}/{pageOffset}/{pageSize}")
    suspend fun getComments(
        @Header("Authorization") auth: String,
        @Path("restaurantId") restaurantId: Long,
        @Path("pageOffset") pageOffset: Int,
        @Path("pageSize") pageSize: Int
    ): CommonResponse<List<RestaurantComment>>
}

object RestaurantApi{
    val retrofitService: RestaurantService by lazy {
        ApiBase.retrofit.create(RestaurantService::class.java)
    }
}