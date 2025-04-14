package com.example.sunseek.model

data class WeatherReport(
    val temp: Float, // Nhiệt độ
    val aqi: Int, // Chất lượng không khí
    val rain: Float?, // Lượng mưa trong 1 tiếng, null nếu không mưa
    val humidity: Int, // Độ ẩm
    val cloud: Int, // Mây
    val detailAddress: String,
    val streetAddress: String
)