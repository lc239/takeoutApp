package com.example.takeoutapplication.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.time.Duration

object ApiBase {
    //应设置拦截器在token过期后统一处理（未完成）
    const val BASE_URL: String = "http://8.130.174.243/api/"
    val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
//        .client(OkHttpClient.Builder().addInterceptor(Interceptor {
//            val origin = it.request()
//            val withContentType = origin.newBuilder().header("Content-Type", "application/json").build()
//            it.proceed(withContentType)
//        }).build()
//        )
        .build()
}