package com.example.hockeygame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil3.asImage
import coil3.load
import coil3.request.crossfade
import com.example.hockeygame.PocketBaseClient.client
import com.example.hockeygame.models.User
import com.example.hockeygame.ui.ClubFragment
import com.example.hockeygame.ui.TeamFragment
import com.example.hockeygame.ui.TournamentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

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

        loadUserData(usernameText, dollarsText, gemsText, avatar)

        avatar.setOnClickListener {
            val personIntent = Intent(this, PersonalActivity::class.java)
            startActivity(personIntent)
        }

        usernameText.setOnClickListener {
            val personIntent = Intent(this, PersonalActivity::class.java)
            startActivity(personIntent)
        }

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

    private fun loadUserData(
        usernameTv: TextView,
        dollarsTv: TextView,
        gemsTv: TextView,
        avatarIv: ImageView
    ) {
        val userId = PocketBaseClient.getUserId()
        if (userId == null) {
            Log.e(TAG, "User ID is null")
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val user = client.records.getOne<User>("users", userId)
                Log.d(TAG, "User loaded: ${user.username}, avatar: ${user.avatar}")

                withContext(Dispatchers.Main) {
                    usernameTv.text = user.username
                    dollarsTv.text = user.dollars.toString()
                    gemsTv.text = user.gems.toString()

                    if (!user.avatar.isNullOrEmpty()) {
                        val avatarUrl = "${PocketBaseClient.BASE_URL}/api/files/users/${user.id}/${user.avatar}"
                        
                        // В Coil 3 нужно явно конвертировать Drawable в Image через .asImage()
                        val placeholderImage = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_news)?.asImage()
                        
                        avatarIv.load(avatarUrl) {
                            crossfade(true)
                            placeholder(placeholderImage)
                            error(placeholderImage)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadUserData: ${e.message}", e)
            }
        }
    }
}
