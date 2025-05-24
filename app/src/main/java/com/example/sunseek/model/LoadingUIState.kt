package com.example.sunseek.model

sealed class LoadingUIState {
    data object Loading : LoadingUIState()
    data object Failed : LoadingUIState()
    data object Idle : LoadingUIState()
    data object Success : LoadingUIState()
}