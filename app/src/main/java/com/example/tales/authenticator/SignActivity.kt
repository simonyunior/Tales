package com.example.tales.authenticator

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tales.Injector
import com.example.tales.MainActivity
import com.example.tales.api.ApiConfiguration
import com.example.tales.databinding.ActivitySignBinding
import com.example.tales.viewmodel.AuthViewModel
import com.example.tales.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this, ViewModelFactory(Injector.provideAuthRepository(this))).get(AuthViewModel::class.java)

        lifecycleScope.launch {
            val token = authViewModel.getToken().first()
            if (!token.isNullOrEmpty()) {
                val intent = Intent(this@SignActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            authViewModel.login(email, password)
        }

        authViewModel.loginResult.observe(this) { result ->
            if (result.isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        val translateAnimation = TranslateAnimation(0f, 0f, 300f, 0f).apply {
            duration = 1000
        }
        val alphaAnimation = AlphaAnimation(0f, 1f).apply {
            duration = 1000
        }

        binding.appLogo.animation = alphaAnimation
        binding.loginButton.animation = translateAnimation
        binding.tvRegister.animation = translateAnimation
    }
}
