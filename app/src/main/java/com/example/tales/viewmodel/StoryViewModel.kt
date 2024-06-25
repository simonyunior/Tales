package com.example.tales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tales.data.StoryRepositoryInterface
import com.example.tales.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repository: StoryRepositoryInterface) : ViewModel() {

    private val _addStoryResult = MutableLiveData<Result<Unit>>()
    val addStoryResult: LiveData<Result<Unit>> get() = _addStoryResult

    fun getStories(): Flow<PagingData<Story>> {
        return repository.getStoriesStream().cachedIn(viewModelScope)
    }

    fun getStoriesWithLocation(): LiveData<List<Story>> {
        val stories = MutableLiveData<List<Story>>()
        viewModelScope.launch {
            try {
                val token = repository.getToken().first()
                if (!token.isNullOrEmpty()) {
                    stories.postValue(repository.getStoriesWithLocation(token))
                } else {
                    stories.postValue(emptyList())
                }
            } catch (e: Exception) {
                stories.postValue(emptyList())
                e.printStackTrace()
            }
        }
        return stories
    }

    fun addStory(description: RequestBody, photo: MultipartBody.Part, lat: RequestBody?, lon: RequestBody?) {
        viewModelScope.launch {
            try {
                val token = repository.getToken().first() ?: ""
                repository.addStory(token, description, photo, lat, lon)
                _addStoryResult.postValue(Result.success(Unit))
            } catch (e: Exception) {
                _addStoryResult.postValue(Result.failure(e))
            }
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            repository.clearToken()
        }
    }

    fun getToken(): Flow<String?> = repository.getToken()
}
