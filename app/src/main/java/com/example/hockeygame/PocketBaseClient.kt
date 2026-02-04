package com.example.hockeygame

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.github.agrevster.pocketbaseKotlin.PocketbaseClient
import io.ktor.http.URLProtocol

object PocketBaseClient {
    private const val TAG = "PocketBaseClient"

    val client = PocketbaseClient({
        protocol = URLProtocol.HTTP
        host = "127.0.0.1"
        port = 8090
    })

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            prefs = EncryptedSharedPreferences.create(
                context,
                "secret_pb_auth",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            restoreSession()
            Log.d(TAG, "Инициализация завершена успешно")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка инициализации: ${e.message}")
        }
    }

    fun saveSession() {
        val token = client.authStore.token
        Log.d(TAG, "Сохранение сессии. Токен найден: ${!token.isNullOrEmpty()}")
        
        if (!token.isNullOrEmpty()) {
            prefs.edit()
                .putString("pb_token", token)
                .apply()
        }
    }

    private fun restoreSession() {
        val savedToken = prefs.getString("pb_token", null)
        Log.d(TAG, "Восстановление сессии. Токен в SharedPreferences: ${if (savedToken != null) "найден" else "пусто"}")
        
        if (!savedToken.isNullOrEmpty()) {
            client.authStore.save(savedToken)
        }
    }

    fun logout() {
        Log.d(TAG, "Выход из системы")
        client.authStore.clear()
        prefs.edit().remove("pb_token").apply()
    }

    fun isAuthenticated(): Boolean {
        val token = client.authStore.token
        val isAuth = !token.isNullOrEmpty()
        Log.d(TAG, "Проверка isAuthenticated: $isAuth (token length: ${token?.length ?: 0})")
        return isAuth
    }
}
