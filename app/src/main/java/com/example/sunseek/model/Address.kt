package com.example.sunseek.model

import java.util.UUID

data class Address(
    val detailedAddress: String,
    val streetAddress: String,
    val id: String = UUID.randomUUID().toString()
)