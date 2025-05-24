package com.example.sunseek.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunseek.BuildConfig
import com.example.sunseek.model.LoadingUIState
import com.example.sunseek.model.Location
import com.example.sunseek.network.GeocodingApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()
    val currentLocation = MutableStateFlow<Location?>(null)
    private val _isCurrentLocation = MutableStateFlow(true)
    private val _isLoading: MutableStateFlow<LoadingUIState> = MutableStateFlow(LoadingUIState.Idle)
    val isLoading = _isLoading.asStateFlow()
    private val geocodeAPIKey = BuildConfig.GEOAPIFY_API_KEY


    suspend fun getLocation(
        context: Context,
        address: String,
        onGetLocationSuccess: (Location) -> Unit
    ) {
        try {
            _isLoading.value = LoadingUIState.Loading
            val response =
                GeocodingApi.geocodingApiService.getCoordinates(address = address, geocodeAPIKey)
            if (response.features.isEmpty()) {
                Toast.makeText(context, "Danh sách rỗng", Toast.LENGTH_SHORT).show()
                _isLoading.value = LoadingUIState.Failed
            } else {
                val properties = response.features[0].properties
                _location.value = Location(
                    longitude = properties.lon,
                    latitude = properties.lat,
                    name = properties.formattedName
                )
                Log.d("Địa điểm nhận được: ", _location.value.toString())
                _isCurrentLocation.value = false
                onGetLocationSuccess(_location.value!!)
                _isLoading.value = LoadingUIState.Success
            }
        } catch (e: Exception) {
            _isLoading.value = LoadingUIState.Failed
            e.printStackTrace()
        } finally {
            _isLoading.value = LoadingUIState.Idle
        }
    }

    suspend fun getAddressName(context: Context, lat: String, lon: String) {
        try {
            _isLoading.value = LoadingUIState.Idle
            val response = GeocodingApi.geocodingApiService.reverseCoordinate(
                lat = lat,
                lon = lon,
                apiKey = geocodeAPIKey
            )
            if (response.features.isEmpty()) {
                Toast.makeText(context, "Danh sách rỗng", Toast.LENGTH_SHORT).show()
                _isLoading.value = LoadingUIState.Failed
            } else {
                val properties = response.features[0].properties
                _location.value = Location(
                    latitude = lat.toDouble(),
                    longitude = lon.toDouble(),
                    name = properties.formattedName
                )
                _isCurrentLocation.value = false
                _isLoading.value = LoadingUIState.Success
            }
        } catch (e: Exception) {
            _isLoading.value = LoadingUIState.Failed
            e.printStackTrace()
        } finally {
            _isLoading.value = LoadingUIState.Idle
        }
    }

    fun setLocationToCurrent() {
        try {
            _location.value = currentLocation.value
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setCurrentLocation(context: Context, latLng: LatLng) {
        viewModelScope.launch {
            try {
                val response = GeocodingApi.geocodingApiService.reverseCoordinate(
                    lat = latLng.latitude.toString(),
                    lon = latLng.longitude.toString(),
                    apiKey = geocodeAPIKey
                )
                _isCurrentLocation.value = true
                if (response.features.isEmpty()) {
                    Toast.makeText(context, "Gán địa chỉ hiện tại thất bại", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val properties = response.features[0].properties
                    currentLocation.value = Location(
                        latitude = latLng.latitude,
                        longitude = latLng.longitude,
                        name = properties.formattedName
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isCurrentLocation(): Boolean {
        return _isCurrentLocation.value
    }
}