package com.example.hockeygame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hockeygame.ui.ClubFragment
import com.example.hockeygame.ui.TeamFragment
import com.example.hockeygame.ui.TournamentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
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
        val avatar = findViewById<ImageView>(R.id.top_bar_avatar)
        val usernameText = findViewById<TextView>(R.id.top_bar_username)
        val dollarsText = findViewById<TextView>(R.id.top_bar_dollars)
        val gemsText = findViewById<TextView>(R.id.top_bar_gems)

        usernameText.text = "DexMP"
        dollarsText.text = "2001"
        gemsText.text = "5"

        avatar.setOnClickListener {
            val personIntent = Intent(this, PersonalActivity::class.java)
            startActivity(personIntent)
        }

        usernameText.setOnClickListener {
            val personIntent = Intent(this, PersonalActivity::class.java)
            startActivity(personIntent)
        }

        // Фрагмент при запуске
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
                        .commit()
                    true
                }
                R.id.bot_nav_tournaments -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TournamentFragment())
                        .commit()
                    true
                }
                R.id.bot_nav_club -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, ClubFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
