package com.example.tales.api

import com.example.tales.model.AddStoryResponse
import com.example.tales.model.LoginResponse
import com.example.tales.model.RegistrationResponse
import com.example.tales.model.Story
import com.example.tales.model.StoryDetailResponse
import com.example.tales.model.StoryResponse
import com.example.tales.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {

    val dummyStories = listOf(
        Story("1", "Title1", "Content1", "photoUrl1", 1.0, 1.0),
        Story("2", "Title2", "Content2", "photoUrl2", 2.0, 2.0)
    )


    override suspend fun register(name: String, email: String, password: String): RegistrationResponse {
        return RegistrationResponse(false, "Success")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return LoginResponse(false, "Success", User("1", "John Doe", email, "fake-token"))
    }

    override suspend fun getStories(token: String, page: Int?, size: Int?, location: Int?): StoryResponse {
        println("Fetching stories with token: $token")
        return StoryResponse(false, "Success", dummyStories)
    }

    override suspend fun getStoryDetail(token: String, id: String): StoryDetailResponse {
        val story = dummyStories.find { it.id == id }
        return StoryDetailResponse(false, "Success", story ?: dummyStories[0])
    }

    override suspend fun addStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponse {
        return AddStoryResponse(false, "Story added successfully")
    }

    override suspend fun getStoriesWithLocation(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): StoryResponse {
        return StoryResponse(false, "Success", dummyStories)
    }
}
