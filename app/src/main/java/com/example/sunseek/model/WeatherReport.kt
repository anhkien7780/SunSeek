package com.example.sunseek.model

import com.example.sunseek.R
import com.example.sunseek.ui.screen.WeatherLevel

data class WeatherReport(
    val temp: Float, // Nhiệt độ
    val aqi: Int, // Chất lượng không khí
    val rain: Float?, // Lượng mưa trong 1 tiếng, null nếu không mưa
    val humidity: Int, // Độ ẩm
    val cloud: Int, // Mây
    val detailAddress: String,
    val streetAddress: String
)

fun WeatherReport.toListWeatherInfo(): List<Information> {
    val temperatureInfo = "${temp.toInt()}℃"
    val temperatureLevel = when {
        temp < 15 -> WeatherLevel.Bad
        temp in 15f..30f -> WeatherLevel.Good
        else -> WeatherLevel.Normal
    }
    val airConditionLevel = when (aqi) {
        in 1..2 -> WeatherLevel.Good
        3 -> WeatherLevel.Normal
        in 4..5 -> WeatherLevel.Bad
        else -> WeatherLevel.Normal
    }
    val cloudLevel = when {
        cloud < 30 -> WeatherLevel.Good
        cloud in 30..70 -> WeatherLevel.Normal
        else -> WeatherLevel.Bad
    }

    val rainLevel = if (rain == 0f) WeatherLevel.Good else WeatherLevel.Bad
    val rainInfo = if (rain == 0f) "Không mưa" else "${rain}mm"

    val humidityLevel = when {
        humidity < 30 -> WeatherLevel.Bad
        humidity in 30..70 -> WeatherLevel.Good
        else -> WeatherLevel.Normal
    }

    return listOf(
        Information(
            R.string.temperature,
            weatherInfo = temperatureInfo,
            weatherLevel = temperatureLevel
        ),
        Information(
            R.string.air_condition,
            weatherInfo = aqi.toString(),
            weatherLevel = airConditionLevel
        ),
        Information(
            R.string.cloud,
            weatherInfo = when {
                cloud < 30 -> "Ít mây"
                cloud in 30..70 -> "Trời quang mây"
                else -> "Nhiều mây"
            },
            weatherLevel = cloudLevel
        ),
        Information(
            R.string.rain,
            weatherInfo = rainInfo,
            weatherLevel = rainLevel
        ),
        Information(
            R.string.humidity,
            weatherInfo = "$humidity%",
            weatherLevel = humidityLevel
        )
    )
}