package com.example.sunseek.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationWithID(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
)
