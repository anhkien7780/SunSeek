package com.example.sunseek.model

import com.example.sunseek.ui.screen.WeatherLevel

data class Information(
    val informationName: Int,
    val weatherInfo: String,
    val weatherLevel: WeatherLevel
)