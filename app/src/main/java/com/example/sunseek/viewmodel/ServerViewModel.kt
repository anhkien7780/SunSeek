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
    private val _isNetworkAvailable = MutableStateFlow<Boolean?>(null)
    val isNetworkAvailable = _isNetworkAvailable.asStateFlow()
    private val _count = MutableStateFlow(0)
    private val timeOutSecond = 30
    private val _isRequestTimeOut = MutableStateFlow(false)
    val isRequestTimeOut = _isRequestTimeOut.asStateFlow()
    fun wakeServer() {
        try {
            viewModelScope.launch {
                while (_count.value < timeOutSecond/5) {
                    val response = SunSeekApi.retrofitService.wake()
                    if (response.isSuccessful) {
                        _isServerRunning.value = true
                        break
                    } else {
                        _count.value++
                    }
                    delay(5000)
                }
                if(!_isServerRunning.value){
                    _isRequestTimeOut.value = true
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

        _isNetworkAvailable.value =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        return _isNetworkAvailable.value!!
    }
}