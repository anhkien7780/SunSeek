package com.example.sunseek.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String
)
//
//fun Location.toAddress(): Address = Address(
//    detailedAddress = this.name.substringBefore(",") ?: "null",
//    streetAddress = this.name.substringAfter(",") ?: "null",
//)