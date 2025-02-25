package com.example.sunseek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sunseek.model.Address
import com.example.sunseek.model.Location



fun Location.toAddress(): Address = Address(
    detailedAddress = this.name.substringBefore(",") ?: "null",
    streetAddress = this.name.substringAfter(",") ?: "null",
)



class FavoriteLocationViewModel : ViewModel() {
    val listLocation = listOf(
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
        Address(
            detailedAddress = "Hồ Tây",
            streetAddress = "Tây hồ, Hà Nội, Việt Nam"
        ),
    )

}

