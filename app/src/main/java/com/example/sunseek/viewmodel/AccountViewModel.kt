package com.example.sunseek.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sunseek.MyApplication
import com.example.sunseek.R
import com.example.sunseek.model.EmailRequest
import com.example.sunseek.model.User
import com.example.sunseek.network.SunSeekApi
import com.example.sunseek.network.clearCookie
import com.example.sunseek.network.saveCookie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed class LoadingUIState {
    data object Loading : LoadingUIState()
    data object Failed : LoadingUIState()
    data object Idle : LoadingUIState()
    data object Success : LoadingUIState()
}

class AccountViewModel : ViewModel() {
    private var _email = mutableStateOf("")
    val email: State<String> = _email
    private val _loadingUIState: MutableStateFlow<LoadingUIState> =
        MutableStateFlow(LoadingUIState.Idle)
    val loadingUIState = _loadingUIState.asStateFlow()
    fun updateUsername(username: String) {
        _email.value = username
    }

    suspend fun register(context: Context, user: User): Boolean {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val response = SunSeekApi.retrofitService.registerUser(user)
            if (response.isSuccessful) {
                Toast.makeText(
                    context,
                    context.getString(R.string.register_success_vn),
                    Toast.LENGTH_SHORT
                ).show()
                _loadingUIState.value = LoadingUIState.Success
                return true
            }
            Toast.makeText(
                context,
                context.getString(R.string.register_failed_vn),
                Toast.LENGTH_SHORT
            ).show()
            _loadingUIState.value = LoadingUIState.Failed
            return false
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                MyApplication.appContext.getString(R.string.register_failed_vn),
                Toast.LENGTH_LONG
            ).show()
        } finally {
            _loadingUIState.value = LoadingUIState.Idle
        }

        return false
    }

    suspend fun login(user: User, context: Context): Boolean {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val response = SunSeekApi.retrofitService.login(user)
            if (response.isSuccessful) {
                val setCookieHeader = response.headers()["Set-Cookie"]
                val userSessionCookie =
                    setCookieHeader?.split(";")?.find { it.trim().startsWith("user-session=") }
                userSessionCookie?.let {
                    Log.d("Cookie received: ", it)
                    saveCookie(context, it)
                }
                return true
            }
            _loadingUIState.value = LoadingUIState.Failed
            Toast.makeText(
                context,
                context.getString(R.string.wrong_password_or_account),
                Toast.LENGTH_LONG
            ).show()
            return false
        } catch (ex: Exception) {
            ex.printStackTrace()
            _loadingUIState.value = LoadingUIState.Failed
            Toast.makeText(
                context,
                context.getString(R.string.server_not_response),
                Toast.LENGTH_LONG
            ).show()
            return false
        } finally {
            _loadingUIState.value = LoadingUIState.Idle
        }
    }

    suspend fun logout(context: Context): Boolean {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val response = SunSeekApi.retrofitService.logout()
            if (response.isSuccessful) {
                clearCookie(MyApplication.appContext)
                Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
                _loadingUIState.value = LoadingUIState.Idle
                return true
            }
            Toast.makeText(context, "Đăng xuất thất bại (Lỗi mạng)", Toast.LENGTH_SHORT).show()
            _loadingUIState.value = LoadingUIState.Idle
            return false
        } catch (ex: Exception) {
            Toast.makeText(context, "Đăng xuất thất bại (Lỗi cục bộ)", Toast.LENGTH_SHORT).show()
            ex.printStackTrace()
            return false
        } finally {
            _loadingUIState.value = LoadingUIState.Idle
        }
    }

    suspend fun forgetPasswordRequest(
        context: Context,
        emailRequest: EmailRequest,
        onRequestSuccess: () -> Unit
    ) {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val response = SunSeekApi.retrofitService.forgotPasswordRequest(emailRequest)
            if (response.isSuccessful) {
                _email.value = emailRequest.email
                Toast.makeText(context, "Gửi xác minh thành công", Toast.LENGTH_SHORT).show()
                _loadingUIState.value = LoadingUIState.Success
                onRequestSuccess()
            } else {
                _loadingUIState.value = LoadingUIState.Failed
                Toast.makeText(context, "Gửi xác minh thất bại", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _loadingUIState.value = LoadingUIState.Failed
        } finally {
            _loadingUIState.value = LoadingUIState.Idle
        }
    }
}