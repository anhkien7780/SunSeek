package com.example.sunseek.network

import com.example.sunseek.MyApplication
import com.example.sunseek.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.1.8:8080"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val okHttpInterceptor =
    OkHttpClient.Builder().addInterceptor(CookieInterceptor(context = MyApplication.appContext))
        .build()
private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(okHttpInterceptor)
        .build()

interface SunSeekApiService {
    @POST("users/add")
    suspend fun registerUser(@Body user: User): Response<Unit>

    @POST("users/login")
    suspend fun login(@Body user: User): Response<Unit>

    @GET("users/logout")
    suspend fun logout(): Response<Unit>
}

object SunSeekApi {
    val retrofitService: SunSeekApiService by lazy {
        retrofit.create(SunSeekApiService::class.java)
    }
}
