package com.example.sunseek.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeRequest(val email: String, val code: String)
