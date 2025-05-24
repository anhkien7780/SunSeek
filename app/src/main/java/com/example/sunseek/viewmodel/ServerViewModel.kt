package com.example.sunseek.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunseek.network.SunSeekApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServerViewModel : ViewModel() {

    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning = _isServerRunning.asStateFlow()
    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable = _isNetworkAvailable.asStateFlow()
    private val _count = MutableStateFlow(0)
    fun wakeServer() {
        try {
            viewModelScope.launch {
                while (_count.value < 3) {
                    val response = SunSeekApi.retrofitService.wake()
                    if (response.isSuccessful) {
                        _isServerRunning.value = true
                        _count.value = 0
                    } else{
                        _count.value++
                    }
                    delay(5000)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        _isNetworkAvailable.value = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        return _isNetworkAvailable.value
    }
}