package com.example.hockeygame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import androidx.lifecycle.lifecycleScope

import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.enums.OAuthProvider
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch



class AuthActivity : AppCompatActivity() {
    companion object {
        fun createClient(context: Context): Client {
            return Client(context.applicationContext)
                .setEndpoint("https://fra.cloud.appwrite.io/v1")
                .setProject("696bc25500156a07c5f6")
        }
    }
    private val account by lazy {
        Account(createClient(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate( savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        findViewById<Button>(R.id.sing_in_btn)?.setOnClickListener {
            lifecycleScope.launch {
                val client = createClient(this@AuthActivity)
                val account = Account(client)
                account.createOAuth2Session(
                    this@AuthActivity,
                    OAuthProvider.GOOGLE,
                    success = "https://fra.cloud.appwrite.io/v1/account/sessions/oauth2/success",
                    failure = "https://fra.cloud.appwrite.io/v1/account/sessions/oauth2/failure",
                    listOf("email", "profile")
                )
            }
        }


    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            try {
                val session = account.get()
                Log.d("Auth", "User logged in: ${session.id}")
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            } catch (e: AppwriteException) {
                Log.e("Auth", "No session: ${e.message}")
            }
        }

    }
}