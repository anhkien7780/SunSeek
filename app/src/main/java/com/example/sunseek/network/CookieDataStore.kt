package com.example.sunseek.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.cookieDataStore by preferencesDataStore("cookie")

object PreferenceKeys {
    val COOKIE = stringPreferencesKey("cookie")
}

fun saveCookie(context: Context, cookie: String) {
    CoroutineScope(Dispatchers.IO).launch {
        context.cookieDataStore.edit { preferences ->
            preferences[PreferenceKeys.COOKIE] = cookie
        }
    }
}

suspend fun clearCookie(context: Context){
    context.cookieDataStore.edit {
        preference ->
        preference.remove(PreferenceKeys.COOKIE)
    }
}