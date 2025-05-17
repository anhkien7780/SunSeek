package com.example.sunseek.model

sealed class WeatherLevel {
    data object Good : WeatherLevel()
    data object Normal : WeatherLevel()
    data object Bad : WeatherLevel()
}