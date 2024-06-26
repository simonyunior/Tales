package com.example.tales.data

import androidx.paging.PagingData
import com.example.tales.api.FakeApiService
import com.example.tales.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryRepository(private val apiService: FakeApiService = FakeApiService()) : StoryRepositoryInterface {

    private var storiesFlow: Flow<PagingData<Story>> = flowOf(PagingData.from(apiService.dummyStories))

    fun overrideGetStoriesStream(newFlow: Flow<PagingData<Story>>) {
        storiesFlow = newFlow
    }

    override fun getStoriesStream(): Flow<PagingData<Story>> {
        println("Returning stories flow with ${apiService.dummyStories.size} stories.")
        return storiesFlow
    }

    override suspend fun addStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
    }

    override suspend fun getStoriesWithLocation(token: String): List<Story> {
        return apiService.dummyStories
    }

    override fun getToken(): Flow<String?> {
        return flowOf("fake-token")
    }

    override suspend fun clearToken() {
        // No-op for testing
    }
}
