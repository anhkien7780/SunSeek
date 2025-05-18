package com.example.sunseek.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().build()

private val openWeatherRetrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface OpenWeatherApiService {
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("air_pollution")
    suspend fun getAirQuality(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): AirQualityResponse
}

object OpenWeatherApi {
    val openWeatherApiService: OpenWeatherApiService by lazy {
        openWeatherRetrofit.create(OpenWeatherApiService::class.java)
    }
}

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val clouds: Clouds,
    val rain: Rain? = null,
    val coord: Coord? = null,
    val wind: Wind? = null,
    val dt: Long? = null,
    val sys: Sys? = null
)

@JsonClass(generateAdapter = true)
data class Coord(
    val lon: Double,
    val lat: Double
)

@JsonClass(generateAdapter = true)
data class Wind(
    val speed: Float,
    val deg: Int,
    val gust: Float? = null
)

@JsonClass(generateAdapter = true)
data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

@JsonClass(generateAdapter = true)
data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

@JsonClass(generateAdapter = true)
data class Main(
    val temp: Float,
    val humidity: Int
)

@JsonClass(generateAdapter = true)
data class Clouds(
    val all: Int
)

@JsonClass(generateAdapter = true)
data class Rain(
    @Json(name = "1h") val oneHour: Float? = null,
    @Json(name = "3h") val threeHour: Float? = null
)
@JsonClass(generateAdapter = true)
data class AirQualityResponse(
    val coord: Coord,
    val list: List<AirQualityData>
)

@JsonClass(generateAdapter = true)
data class AirQualityData(
    val main: AirQualityIndex,
    val components: Components,
    val dt: Long
)

@JsonClass(generateAdapter = true)
data class AirQualityIndex(
    val aqi: Int // AQI từ 1 đến 5
)

@JsonClass(generateAdapter = true)
data class Components(
    val co: Float,
    val no: Float,
    val no2: Float,
    val o3: Float,
    val so2: Float,
    @Json(name = "pm2_5") val pm2_5: Float,
    val pm10: Float,
    val nh3: Float
)
