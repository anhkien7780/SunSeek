package com.example.sunseek.model

import com.example.sunseek.MyApplication
import com.example.sunseek.R

data class Description(
    val informationName: Int,
    val information: Int
)


val notes = listOf(
    MyApplication.appContext.getString(R.string.tip_1),
    MyApplication.appContext.getString(R.string.tip_2),
    MyApplication.appContext.getString(R.string.tip_3),
    MyApplication.appContext.getString(R.string.tip_4)
)


val listDescription = listOf(
    Description(
        informationName = R.string.temperature,
        information = R.string.temperature_description
    ),
    Description(
        informationName = R.string.air_condition,
        information = R.string.air_conditional_description
    ),
    Description(
        informationName = R.string.cloud,
        information = R.string.cloud_description
    ),
    Description(
        informationName = R.string.rain,
        information = R.string.rain_description
    ),
    Description(
        informationName = R.string.humidity,
        information = R.string.humidity_description
    )
)