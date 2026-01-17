package com.example.hockeygame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.bot_nav_team -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.bot_nav_tournaments -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.bot_nav_club -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }
}