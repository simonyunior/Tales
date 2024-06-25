package com.example.tales.data

import androidx.paging.PagingData
import com.example.tales.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryRepository : StoryRepositoryInterface {
    private val dummyStories = listOf(
        Story("1", "Title1", "Content1", "photoUrl1", 1.0, 1.0),
        Story("2", "Title2", "Content2", "photoUrl2", 2.0, 2.0)
    )

    private var storiesFlow: Flow<PagingData<Story>> = flowOf(PagingData.from(dummyStories))

    override fun getStoriesStream(): Flow<PagingData<Story>> {
        return storiesFlow
    }

    fun overrideGetStoriesStream(newFlow: Flow<PagingData<Story>>) {
        storiesFlow = newFlow
    }

    @Suppress("UNUSED_PARAMETER")
    override suspend fun addStory(token: String, description: RequestBody, photo: MultipartBody.Part, lat: RequestBody?, lon: RequestBody?) {
    }

    @Suppress("UNUSED_PARAMETER")
    override suspend fun getStoriesWithLocation(token: String): List<Story> {
        return dummyStories
    }

    override fun getToken(): Flow<String?> {
        return flowOf("fake-token")
    }

    override suspend fun clearToken() {
    }
}
