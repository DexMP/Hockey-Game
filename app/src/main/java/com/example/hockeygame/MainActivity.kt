package com.example.hockeygame

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hockeygame.ui.TeamFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Загружаем первый фрагмент при запуске
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, TeamFragment())
                .commit()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bot_nav_team -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TeamFragment())
                        // ✅ Убрал addToBackStack, чтобы избежать накопления фрагментов
                        .commit()
                    true
                }
                R.id.bot_nav_tournaments -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TeamFragment())
                        // ✅ Убрал addToBackStack, чтобы избежать накопления фрагментов
                        .commit()
                    true
                }
                R.id.bot_nav_club -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TeamFragment())
                        // ✅ Убрал addToBackStack, чтобы избежать накопления фрагментов
                        .commit()
                    true
                }
                // ✅ Добавь сюда другие пункты меню, когда создашь их фрагменты
                else -> false
            }
        }
    }
}
