package com.example.sunseek

import com.example.sunseek.network.GeocodingApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GeocodeAPITest{
    private val geocodeAPIKey = BuildConfig.GEOAPIFY_API_KEY
    @Test
    fun testGeocoding() = runBlocking{
        val response = GeocodingApi.geocodingApiService.getCoordinates(address = "Hồ Tây, Việt Nam", geocodeAPIKey)
        assert(response.features.isNotEmpty())
        println(response.features)
    }

    @Test
    fun testReverseGeocoding() = runBlocking{
        val response = GeocodingApi.geocodingApiService.reverseCoordinate(lat = 21.05805795.toString(), lon = 105.81396229164264.toString(), geocodeAPIKey)
        assert(response.features.isNotEmpty())
        println(response.features)
    }
}