package com.example.sunseek.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://maps.googleapis.com/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val geoApiRetrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

interface GeocodingApiService{
    @GET("maps/api/geocode/json")
    suspend fun getCoordinates(
        @Query("address") address: String,
        @Query("key") apiKey: String,
    ): GeocodingResponse

    @GET("maps/api/geocode/json")
    suspend fun getFormattedAddress(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String,
    ): ReverseGeoResponse
}

object GeocodingApi{
    val geocodingApiService: GeocodingApiService by lazy {
        geoApiRetrofit.create(GeocodingApiService::class.java)
    }
}

@JsonClass(generateAdapter = true)
data class RespondedLocation(
    val lat: Double,
    val lng: Double
)

@JsonClass(generateAdapter = true)
data class Geometry(
    val location: RespondedLocation
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "formatted_address") val formattedAddress: String,
    val geometry: Geometry
)

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    val results: List<Result>?,
    @Json(name = "status") val status: String,
    @Json(name = "error_message") val errorMessage: String? = null
)

@JsonClass(generateAdapter = true)
data class ReverseGeoResponse(
    val results: List<ReverseGeoResponseResult>?,
    val status: String
)

@JsonClass(generateAdapter = true)
data class ReverseGeoResponseResult(
    @Json(name = "formatted_address") val formattedAddress: String
)