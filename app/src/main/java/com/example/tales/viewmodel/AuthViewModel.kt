package com.example.tales.viewmodel

import androidx.lifecycle.*
import com.example.tales.data.AuthRepository
import com.example.tales.model.LoginResponse
import com.example.tales.model.RegistrationResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _registerResult = MutableLiveData<Result<RegistrationResponse>>()
    val registerResult: LiveData<Result<RegistrationResponse>> = _registerResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResult.value = Result.success(response)
                repository.saveToken(response.loginResult.token)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                _registerResult.value = Result.success(response)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }

    fun getToken(): Flow<String?> {
        return repository.getToken()
    }

    suspend fun saveToken(token: String) {
        repository.saveToken(token)
    }
}
