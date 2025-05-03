package com.example.sunseek.network

import com.example.sunseek.MyApplication
import com.example.sunseek.model.EmailRequest
import com.example.sunseek.model.Location
import com.example.sunseek.model.LocationWithID
import com.example.sunseek.model.ResetPasswordRequest
import com.example.sunseek.model.User
import com.example.sunseek.model.VerifyCodeRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Local host
//private const val ip = "192.168.1.9"
//private const val BASE_URL = "http://${ip}:8080"

// Render host
private const val BASE_URL = "https://sunseek-server.onrender.com/"

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

    @POST("users/logout")
    suspend fun logout(): Response<Unit>

    @GET("users/locations/list")
    suspend fun getListLocation(): Response<List<LocationWithID>>

    @POST("users/locations/add")
    suspend fun addLocation(@Body location: Location): Response<Unit>

    @DELETE("users/locations/delete/{id}")
    suspend fun deleteLocation(@Path("id") locationID: Int): Response<Unit>

    @POST("users/forgot_password")
    suspend fun forgotPasswordRequest(@Body email: EmailRequest): Response<Unit>

    @POST("users/verify_code")
    suspend fun verifyCodeRequest(@Body request: VerifyCodeRequest): Response<Boolean>

    @POST("users/reset_password")
    suspend fun resetPasswordRequest(@Body resetInfo: ResetPasswordRequest): Response<Unit>
}


object SunSeekApi {
    val retrofitService: SunSeekApiService by lazy {
        retrofit.create(SunSeekApiService::class.java)
    }
}
