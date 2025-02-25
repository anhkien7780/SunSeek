package com.example.sunseek.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sunseek.MyApplication
import com.example.sunseek.model.User
import com.example.sunseek.network.SunSeekApi
import com.example.sunseek.network.clearCookie
import com.example.sunseek.network.saveCookie
import kotlinx.coroutines.flow.MutableStateFlow


sealed class LoginUIState {
    data object Loading : LoginUIState()
    data object Failed : LoginUIState()
    data object Idle : LoginUIState()

}

class AccountViewModel : ViewModel() {
    private var _username = mutableStateOf("")
    val username: State<String> = _username
    fun updateUsername(username: String) {
        _username.value = username
    }
    private var _loginUIState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState.Idle)
    val loginUIState = _loginUIState.value

    fun updateLoginUIState(loginUIState: LoginUIState) {
        _loginUIState.value = loginUIState
    }

    suspend fun register(user: User): Boolean {
        val response = SunSeekApi.retrofitService.registerUser(user)
        return response.isSuccessful
    }

    suspend fun login(user: User, context: Context): Boolean {
        val response = SunSeekApi.retrofitService.login(user)
        if (response.isSuccessful) {
            val cookies = response.headers()
            val cookieString = cookies.joinToString(";")
            saveCookie(context, cookieString)
            return true
        }
        return false
    }
    suspend fun logout(): Boolean {
        val response = SunSeekApi.retrofitService.logout()
        if (response.isSuccessful) {
            clearCookie(MyApplication.appContext)
            return true
        }
        return false
    }
}