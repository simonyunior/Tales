package com.example.tales

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.tales.data.FakeStoryRepository
import com.example.tales.model.Story
import com.example.tales.viewmodel.FakeStoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoryViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var fakeRepository: FakeStoryRepository
    private lateinit var storyViewModel: FakeStoryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeStoryRepository()
        storyViewModel = FakeStoryViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher to the original Main dispatcher
        dispatcher.cancel() // Cancel the test dispatcher
    }

    @Test
    fun `when getStories called with data then return data`() = runTest {
        val result = storyViewModel.getStories().single()
        assertNotNull(result)

        // Convert PagingData to List for assertions
        val resultList = result.collectData()

        // Log the size and contents of the result list
        println("Result list size: ${resultList.size}")
        resultList.forEachIndexed { index, story ->
            println("Story $index: ${story.id}")
        }

        assertEquals(2, resultList.size)
        assertEquals("1", resultList[0].id)
    }

    @Test
    fun `when getStories called with no data then return empty`() = runTest {
        val emptyFlow = flowOf(PagingData.empty<Story>())
        fakeRepository.overrideGetStoriesStream(emptyFlow)

        val result = storyViewModel.getStories().single()
        assertNotNull(result)

        // Convert PagingData to List for assertions
        val resultList = result.collectData()

        // Log the size of the result list
        println("Result list size: ${resultList.size}")

        assertEquals(0, resultList.size)
    }
}
