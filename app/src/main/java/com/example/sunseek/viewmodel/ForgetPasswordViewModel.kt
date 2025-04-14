package com.example.sunseek.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.sunseek.R
import com.example.sunseek.model.ResetPasswordRequest
import com.example.sunseek.model.VerifyCodeRequest
import com.example.sunseek.network.SunSeekApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgetPasswordViewModel : ViewModel() {
    val _code = MutableStateFlow<String?>(null)
    val _email = MutableStateFlow<String?>(null)
    val code = _code.asStateFlow()
    val email = _email.asStateFlow()
    private val _loadingUIState: MutableStateFlow<LoadingUIState> =
        MutableStateFlow(LoadingUIState.Idle)
    val loadingUIState = _loadingUIState.asStateFlow()
    fun setIdle(){
        _loadingUIState.value = LoadingUIState.Idle
    }
    suspend fun verifyCode(context: Context, verifyCodeRequest: VerifyCodeRequest) {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            val response = SunSeekApi.retrofitService.verifyCodeRequest(request = verifyCodeRequest)
            if (response.isSuccessful) {
                val isCorrectCode: Boolean = response.body() == true
                if (isCorrectCode) {
                    _code.value = verifyCodeRequest.code
                    _email.value = verifyCodeRequest.email
                    _loadingUIState.value = LoadingUIState.Success
                    Toast.makeText(context,
                        context.getString(R.string.confirm_success), Toast.LENGTH_SHORT).show()
                } else {
                    _loadingUIState.value = LoadingUIState.Failed
                    Toast.makeText(context,
                        context.getString(R.string.code_incorrect), Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _loadingUIState.value = LoadingUIState.Failed
        } finally {
            if (_loadingUIState.value != LoadingUIState.Success)
                _loadingUIState.value = LoadingUIState.Idle
        }
    }
    
    suspend fun changePasswordRequest(context: Context, newPassword: String) {
        try {
            _loadingUIState.value = LoadingUIState.Loading
            Log.d("ResetPasswordRequest", "newPassword=${newPassword}, code=${code.value}, email=${email.value}")

            val response = SunSeekApi.retrofitService.resetPasswordRequest(resetInfo = ResetPasswordRequest(
                newPassword = newPassword, code = code.value!!, email = email.value!!
            ))
            if (response.isSuccessful) {
                Toast.makeText(context,
                    context.getString(R.string.change_password_success), Toast.LENGTH_SHORT).show()
                _loadingUIState.value = LoadingUIState.Success
            } else{
                Toast.makeText(context,
                    context.getString(R.string.change_password_failed), Toast.LENGTH_SHORT).show()
                _loadingUIState.value = LoadingUIState.Failed
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _loadingUIState.value = LoadingUIState.Failed
        } finally {
            if (_loadingUIState.value != LoadingUIState.Success)
                _loadingUIState.value = LoadingUIState.Idle
        }
    }

}