package com.example.hockeygame

import android.content.Intent
import android.os.Bundle
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

        binding.btnNext.setOnClickListener {
            navigateToMain()
        }

        if (PocketBaseClient.isAuthenticated()) {
            navigateToMain()
            return
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                login(email, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()

            if (validateRegistration(email, password, username)) {
                register(email, password, username)
            }
        }
    }

    private fun login(email: String, password: String) {
        binding.btnLogin.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Выполняем вход и получаем результат
                val authData = client.records.authWithPassword<AuthRecord>("users", email, password)
                
                // ВАЖНО: Вручную сохраняем токен в authStore клиента
                client.authStore.save(authData.token)
                
                // Теперь сохраняем сессию в SharedPreferences
                PocketBaseClient.saveSession()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AuthActivity, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this@AuthActivity, "Ошибка входа: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun register(email: String, password: String, username: String) {
        binding.btnRegister.isEnabled = false

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

                // После регистрации выполняем вход
                val authData = client.records.authWithPassword<AuthRecord>("users", email, password)
                
                // Вручную сохраняем токен
                client.authStore.save(authData.token)

                PocketBaseClient.saveSession()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AuthActivity, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this@AuthActivity, "Ошибка регистрации: ${e.message}", Toast.LENGTH_LONG).show()
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
