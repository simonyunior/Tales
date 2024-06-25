package com.example.tales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.tales.data.FakeStoryRepository
import com.example.tales.model.Story
import kotlinx.coroutines.flow.Flow

class FakeStoryViewModel(private val repository: FakeStoryRepository) : ViewModel() {
    fun getStories(): Flow<PagingData<Story>> {
        return repository.getStoriesStream()
    }
}
