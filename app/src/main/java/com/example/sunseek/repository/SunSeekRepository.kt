package com.example.sunseek.repository

import com.example.sunseek.model.LocationWithID

object SunSeekRepository : SunSeekRepo {
    private val listLocation = mutableListOf<LocationWithID>()
    override fun addLocation(location: LocationWithID) {
        listLocation.add(location)
    }

    override fun removeLocation(locationWithID: LocationWithID) =
        listLocation.remove(locationWithID)
}