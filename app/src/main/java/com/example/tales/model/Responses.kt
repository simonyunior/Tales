package com.example.tales.model

import com.example.tales.model.Story
import com.example.tales.model.User

data class RegistrationResponse(
    val error: Boolean,
    val message: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: User
)

data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)

data class AddStoryResponse(
    val error: Boolean,
    val message: String
)

data class StoryDetailResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)
