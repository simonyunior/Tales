package com.example.tales.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tales.api.ApiService
import com.example.tales.model.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : StoryRepositoryInterface {

    private var token: String? = null

    fun setToken(newToken: String) {
        token = newToken
    }

    override fun getStoriesStream(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token ?: "")
            }
        ).flow
    }

    override suspend fun addStory(token: String, description: RequestBody, photo: MultipartBody.Part, lat: RequestBody?, lon: RequestBody?) {
        apiService.addStory("Bearer $token", photo, description, lat, lon)
    }

    override suspend fun getStoriesWithLocation(token: String): List<Story> {
        val response = apiService.getStories("Bearer $token", 1, 20, 1)
        return response.listStory
    }

    override fun getToken(): Flow<String?> = userPreferences.getToken()

    override suspend fun clearToken() {
        userPreferences.clearToken()
    }
}
