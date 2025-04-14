package com.example.sunseek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sunseek.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : ViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()
//    fun updateLocation(address: String) {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                Log.d("MapViewModel", "Calling API with address: $address, API Key: ${BuildConfig.MAPS_API_KEY}")
//                val response = GeocodingApi.geocodingApiService.getCoordinates(address, BuildConfig.MAPS_API_KEY)
//                Log.d("MapViewModel", "API Response: status=${response.status}, results=${response.results}")
//                if(response.status == "OK" && response.results?.isNotEmpty() == true){
//                    val firstResult = response.results.first()
//                    val location = firstResult.geometry.location
//                    val formattedAddress = firstResult.formattedAddress
//                    _location.value = Location(location.lat, location.lng, formattedAddress)
//                } else{
//                    Log.d("MapViewModel", "Invalid response: status=${response.status}, error=${response.errorMessage}")
//                    _location.value = null
//                }
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//                _location.value = null
//                Log.e("MapViewModel", "API Error: ${ex.message}", ex)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//    fun updateFormatedAddress(latLng: LatLng){
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                val latLngString = "${latLng.latitude}, ${latLng.longitude}"
//                val response = GeocodingApi.geocodingApiService.getFormattedAddress(latLngString, BuildConfig.MAPS_API_KEY)
//                if(response.status == "OK"){
//                    val formattedAddress = response.results?.first()?.formattedAddress
//                    if(formattedAddress != null){
//                        _location.value = Location(latLng.latitude, latLng.longitude, name = formattedAddress)
//                    }
//                }
//            } catch (ex: Exception){
//                ex.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
}