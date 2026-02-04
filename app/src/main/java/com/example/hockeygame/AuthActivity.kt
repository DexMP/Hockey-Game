package com.example.hockeygame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hockeygame.PocketBaseClient.client
import com.example.hockeygame.databinding.ActivityAuthBinding
import com.example.hockeygame.models.User
import io.github.agrevster.pocketbaseKotlin.models.AuthRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (PocketBaseClient.isAuthenticated()) {
            navigateToMain()
            return
        }

        setupListeners()
        updateUI()
    }

    private fun setupListeners() {
        binding.btnMainAction.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()

            if (isLoginMode) {
                if (validateInput(email, password)) {
                    login(email, password)
                }
            } else {
                val username = binding.etUsername.text.toString().trim()
                if (validateRegistration(email, password, username)) {
                    register(email, password, username)
                }
            }
        }

        binding.btnToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUI()
        }

        binding.btnNext.setOnClickListener {
            navigateToMain()
        }
    }

    private fun updateUI() {
        if (isLoginMode) {
            binding.tilUsername.visibility = View.GONE
            binding.btnMainAction.text = "Войти"
            binding.btnToggleMode.text = "Нет аккаунта? Зарегистрироваться"
        } else {
            binding.tilUsername.visibility = View.VISIBLE
            binding.btnMainAction.text = "Создать аккаунт"
            binding.btnToggleMode.text = "Уже есть аккаунт? Войти"
        }
    }

    private fun login(email: String, password: String) {
        binding.btnMainAction.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val authData = client.records.authWithPassword<AuthRecord>("users", email, password)
                client.authStore.save(authData.token)
                
                // Сохраняем ID пользователя для дальнейших запросов
                PocketBaseClient.saveSession(authData.record.id)

                withContext(Dispatchers.Main) {
                    navigateToMain()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.btnMainAction.isEnabled = true
                    Toast.makeText(this@AuthActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun register(email: String, password: String, username: String) {
        binding.btnMainAction.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userData = mapOf(
                    "email" to email,
                    "password" to password,
                    "passwordConfirm" to password,
                    "username" to username
                )

                val jsonBody = kotlinx.serialization.json.Json.encodeToString(userData)
                client.records.create<User>("users", jsonBody)

                val authData = client.records.authWithPassword<AuthRecord>("users", email, password)
                client.authStore.save(authData.token)
                
                // Сохраняем ID после регистрации
                PocketBaseClient.saveSession(authData.record.id)

                withContext(Dispatchers.Main) {
                    navigateToMain()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.btnMainAction.isEnabled = true
                    Toast.makeText(this@AuthActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Введите email"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Введите пароль"
            return false
        }
        return true
    }

    private fun validateRegistration(email: String, password: String, username: String): Boolean {
        if (!validateInput(email, password)) return false
        if (username.isEmpty()) {
            binding.etUsername.error = "Введите имя пользователя"
            return false
        }
        return true
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
