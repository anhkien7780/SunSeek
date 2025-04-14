package com.example.sunseek.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunseek.MyApplication
import com.example.sunseek.R
import com.example.sunseek.model.Location
import com.example.sunseek.model.LocationWithID
import com.example.sunseek.network.SunSeekApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LocationViewModel : ViewModel() {
    private var _listLocation = MutableStateFlow<List<LocationWithID>>(emptyList())
    private val _loadingUIState: MutableStateFlow<LoadingUIState> =
        MutableStateFlow(LoadingUIState.Idle)
    val loadingUIState = _loadingUIState.asStateFlow()
    val listLocation = _listLocation.asStateFlow()
    private val _isLocationListEmpty = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            _listLocation.collect { list ->
                _isLocationListEmpty.value = list.isEmpty()
            }
        }
    }

    fun getLastLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onLocationReceived: (LatLng) -> Unit,
        onPermissionDenied: () -> Unit = {}
    ) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    MyApplication.appContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    MyApplication.appContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                onPermissionDenied()
            }
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    onLocationReceived(latLng)
                }
            }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    suspend fun getListLocation(context: Context) {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val response = SunSeekApi.retrofitService.getListLocation()
            if (response.isSuccessful) {
                _listLocation.value = response.body()!!
                _loadingUIState.value = LoadingUIState.Success
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.collect_location_info_falied), Toast.LENGTH_SHORT
                ).show()
                _loadingUIState.value = LoadingUIState.Failed
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            _loadingUIState.value = LoadingUIState.Failed
        } finally {
            _loadingUIState.value = LoadingUIState.Idle
        }

    }

    suspend fun addAddress(context: Context, location: Location, onSuccess: () -> Unit) {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val respond = SunSeekApi.retrofitService.addLocation(location)
            if (respond.isSuccessful) {
                Toast.makeText(
                    context,
                    "Tải địa chỉ thành công!!!",
                    Toast.LENGTH_SHORT
                ).show()
                _loadingUIState.value = LoadingUIState.Success
                onSuccess()
            } else {
                _loadingUIState.value = LoadingUIState.Failed
                Toast.makeText(
                    context,
                    "Tải địa chỉ lên server không thành công. Vui lòng thử lại!!!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("Respond", respond.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _loadingUIState.value = LoadingUIState.Failed
        } finally {
            _loadingUIState.value = LoadingUIState.Idle
        }

    }
}

