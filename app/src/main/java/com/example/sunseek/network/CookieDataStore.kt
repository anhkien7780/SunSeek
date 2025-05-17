package com.example.sunseek.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.cookieDataStore by preferencesDataStore("cookie")

object PreferenceKeys {
    val COOKIE = stringPreferencesKey("cookie")
}

suspend fun saveCookie(context: Context, cookie: String) {
    context.cookieDataStore.edit { preferences ->
        preferences[PreferenceKeys.COOKIE] = cookie
    }
}

suspend fun clearCookie(context: Context) {
    context.cookieDataStore.edit { preference ->
        preference.remove(PreferenceKeys.COOKIE)
    }
}