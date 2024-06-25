package com.example.tales.data

import android.util.Log
import com.example.tales.api.ApiService
import com.example.tales.model.LoginResponse
import com.example.tales.model.RegistrationResponse
import com.example.tales.model.User
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val apiService: ApiService, private val userPreferences: UserPreferences) {

    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        Log.d("AuthRepository", "Login token: ${response.loginResult.token}")
        saveUser(response.loginResult)
        saveToken(response.loginResult.token)
        return response
    }

    suspend fun register(name: String, email: String, password: String): RegistrationResponse {
        return apiService.register(name, email, password)
    }

    suspend fun saveUser(user: User) {
        userPreferences.saveUser(user)
    }

    suspend fun saveToken(token: String) {
        Log.d("AuthRepository", "Saving token: $token")
        userPreferences.saveToken(token)
    }

    fun getToken(): Flow<String?> {
        return userPreferences.getToken()
    }

    suspend fun clearSession() {
        userPreferences.clearSession()
    }
}
