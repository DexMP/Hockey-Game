package com.example.hockeygame

import io.github.agrevster.pocketbaseKotlin.PocketbaseClient
import io.ktor.http.URLProtocol

object PocketBaseClient {
    val client = PocketbaseClient({
        protocol = URLProtocol.HTTP
        host = "localhost"
        port = 8090
    })

    // Проверка авторизации
    fun isAuthenticated(): Boolean {
        return client.authStore.token?.isNotEmpty() == true
    }
}