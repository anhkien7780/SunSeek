package com.example.sunseek.repository

import com.example.sunseek.model.LocationWithID

interface SunSeekRepo {
    fun addLocation(location: LocationWithID)
    fun removeLocation(locationWithID: LocationWithID): Boolean
}