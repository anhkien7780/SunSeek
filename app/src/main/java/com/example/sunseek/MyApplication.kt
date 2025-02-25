package com.example.sunseek

import android.app.Application

class MyApplication:  Application(){
    companion object {
        lateinit var instance: MyApplication
            private set
        val appContext get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}