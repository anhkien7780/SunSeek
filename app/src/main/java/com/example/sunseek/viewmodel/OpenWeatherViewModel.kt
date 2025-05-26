package com.example.sunseek.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunseek.BuildConfig
import com.example.sunseek.MyApplication
import com.example.sunseek.model.Address
import com.example.sunseek.model.LoadingUIState
import com.example.sunseek.model.WeatherReport
import com.example.sunseek.network.OpenWeatherApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class OpenWeatherViewModel : ViewModel() {
    private val _weatherReport = MutableStateFlow<WeatherReport?>(null)
    val weatherReportImageUrl = MutableStateFlow<Uri?>(null)
    val weatherReportImageBitmap = MutableStateFlow<Bitmap?>(null)
    val weatherReport = _weatherReport.asStateFlow()
    private val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
    private val _weatherUIState: MutableStateFlow<LoadingUIState> =
        MutableStateFlow(LoadingUIState.Idle)
    val weatherUIState = _weatherUIState.asStateFlow()
    fun getWeatherReportByCoordinates(lat: Double, lon: Double, address: Address) {
        viewModelScope.launch {
            _weatherUIState.value = LoadingUIState.Loading
            try {

                val weather =
                    OpenWeatherApi.openWeatherApiService.getWeatherByCoordinates(lat, lon, apiKey)

                val air = OpenWeatherApi.openWeatherApiService.getAirQuality(lat, lon, apiKey)

                val temp = weather.main.temp
                val humidity = weather.main.humidity
                val cloud = weather.clouds.all
                val rain = weather.rain?.oneHour ?: 0f
                val description = weather.weather.firstOrNull()?.description ?: "N/A"
                val aqi = air.list.firstOrNull()?.main?.aqi ?: -1


                _weatherReport.value = WeatherReport(
                    temp = temp,
                    aqi = aqi,
                    rain = rain,
                    humidity = humidity,
                    cloud = cloud,
                    detailAddress = address.detailedAddress,
                    streetAddress = address.streetAddress
                )
                _weatherUIState.value = LoadingUIState.Success
            } catch (e: Exception) {
                Toast.makeText(
                    MyApplication.appContext,
                    "Lấy thông tin thời tiết thất bại",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("WeatherError", "Lỗi xảy ra khi lấy dữ liệu: ${e.message}", e)
                _weatherUIState.value = LoadingUIState.Failed
            } finally {
                Log.d("WeatherFetch", "Hoàn thành gọi API")
                Toast.makeText(
                    MyApplication.appContext,
                    "Lấy thông tin thời tiết thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
    fun setIdle(){
        _weatherUIState.value = LoadingUIState.Idle
    }

    fun onLogout(){
        _weatherReport.value = null
        weatherReportImageUrl.value = null
        weatherReportImageBitmap.value = null
    }
}