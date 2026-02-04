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
    
    // Используй 127.0.0.1 если делаешь adb reverse
    // Или IP компьютера (например 192.168.1.X) если через Wi-Fi
    const val BASE_URL = "http://127.0.0.1:8090"

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
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка инициализации: ${e.message}")
        }
    }

    fun saveSession(userId: String? = null) {
        val token = client.authStore.token
        if (!token.isNullOrEmpty()) {
            val editor = prefs.edit().putString("pb_token", token)
            if (userId != null) {
                editor.putString("pb_user_id", userId)
            }
            editor.apply()
        }
    }

    private fun restoreSession() {
        val savedToken = prefs.getString("pb_token", null)
        if (!savedToken.isNullOrEmpty()) {
            client.authStore.save(savedToken)
        }
    }

    fun getUserId(): String? {
        return prefs.getString("pb_user_id", null)
    }

    fun logout() {
        client.authStore.clear()
        prefs.edit().clear().apply()
    }

    fun isAuthenticated(): Boolean {
        return !client.authStore.token.isNullOrEmpty()
    }
}
