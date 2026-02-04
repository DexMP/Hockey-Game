package com.example.hockeygame

import android.app.Application

class HockeyGameApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PocketBaseClient.init(this)
    }
}
