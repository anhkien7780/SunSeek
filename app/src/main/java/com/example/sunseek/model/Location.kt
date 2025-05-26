package com.example.sunseek.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String
){
    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}