package com.example.tales.authenticator

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tales.Injector
import com.example.tales.databinding.ActivityRegisterBinding
import com.example.tales.model.RegistrationResponse
import com.example.tales.viewmodel.AuthViewModel
import com.example.tales.viewmodel.ViewModelFactory

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this, ViewModelFactory(Injector.provideAuthRepository(this))).get(AuthViewModel::class.java)

        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            authViewModel.register(name, email, password)
        }

        authViewModel.registerResult.observe(this) { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Registration successful. Please log in.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }

        val translateAnimation = TranslateAnimation(0f, 0f, 300f, 0f).apply {
            duration = 1000
        }
        val alphaAnimation = AlphaAnimation(0f, 1f).apply {
            duration = 1000
        }

        binding.appLogo.animation = alphaAnimation
        binding.registerButton.animation = translateAnimation
        binding.edRegisterName.animation = translateAnimation
        binding.edRegisterEmail.animation = translateAnimation
        binding.edRegisterPassword.animation = translateAnimation
    }
}

