package com.example.sunseek.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val cookie = runBlocking {
            context.cookieDataStore.data.map { it[PreferenceKeys.COOKIE]}.firstOrNull()
        }
        val newRequest = originalRequest.newBuilder().apply {
            cookie?.let {
                addHeader("Cookie", it)
            }
        }.build()
        Log.d("CookieInterceptor", "Cookie: $cookie")
        return chain.proceed(newRequest)
    }

}