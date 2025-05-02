package com.example.sunseek.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.geoapify.com/v1/geocode/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val geoApiRetrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

interface GeocodingApiService{
    @GET("search")
    suspend fun getCoordinates(
        @Query("text") address: String,
        @Query("apiKey") apiKey: String,
    ): GeocodingResponse

    @GET("reverse")
    suspend fun reverseCoordinate(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("apiKey") apiKey: String,
    ): ReverseGeocodeResponse
}

object GeocodingApi{
    val geocodingApiService: GeocodingApiService by lazy {
        geoApiRetrofit.create(GeocodingApiService::class.java)
    }
}

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    @Json(name = "type") val type: String,
    @Json(name = "features") val features: List<GeocodeFeature>
)

@JsonClass(generateAdapter = true)
data class GeocodeFeature(
    @Json(name = "properties") val properties: GeocodeProperties
)

@JsonClass(generateAdapter = true)
data class GeocodeProperties(
    @Json(name = "lon") val lon: Double,
    @Json(name = "lat") val lat: Double,
    @Json(name = "formatted") val formattedName: String
)

@JsonClass(generateAdapter = true)
data class ReverseGeocodeResponse(
    @Json(name = "type") val type: String,
    @Json(name = "features") val features: List<ReverseGeocodeFeature>
)

@JsonClass(generateAdapter = true)
data class ReverseGeocodeFeature(
    @Json(name = "properties") val properties: ReverseGeocodeProperties
)

@JsonClass(generateAdapter = true)
data class ReverseGeocodeProperties(
    @Json(name = "formatted") val formattedName: String
)