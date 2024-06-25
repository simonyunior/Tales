package com.example.tales.data

import androidx.paging.PagingData
import com.example.tales.model.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepositoryInterface {
    fun getStoriesStream(): Flow<PagingData<Story>>
    fun getToken(): Flow<String?>
    suspend fun getStoriesWithLocation(token: String): List<Story>
    suspend fun addStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    )
    suspend fun clearToken()
}
