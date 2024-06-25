package com.example.tales

import android.content.Context
import com.example.tales.api.ApiConfiguration
import com.example.tales.data.AuthRepository
import com.example.tales.data.StoryRepository
import com.example.tales.data.UserPreferences

object Injector {
    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfiguration.getApiService()
        val userPreferences = UserPreferences.getInstance(context)
        return AuthRepository(apiService, userPreferences)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfiguration.getApiService()
        val userPreferences = UserPreferences.getInstance(context)
        return StoryRepository(apiService, userPreferences)
    }
}
