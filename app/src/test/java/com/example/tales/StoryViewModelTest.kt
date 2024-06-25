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

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
    }

    @Test
    fun `when getStories called with data then return data`() = runTest {
        val fakeRepository = FakeStoryRepository()
        val storyViewModel = FakeStoryViewModel(fakeRepository)

        val result = storyViewModel.getStories().single()
        assertNotNull(result)


        val resultList = result.collectData()

        println("Result list size: ${resultList.size}")
        resultList.forEachIndexed { index, story ->
            println("Story $index: ${story.id}")
        }

        assertEquals(2, resultList.size)
        assertEquals("1", resultList[0].id)
    }

    @Test
    fun `when getStories called with no data then return empty`() = runTest {
        val fakeRepository = FakeStoryRepository()
        val storyViewModel = FakeStoryViewModel(fakeRepository)

        val emptyFlow = flowOf(PagingData.empty<Story>())
        fakeRepository.overrideGetStoriesStream(emptyFlow)

        val result = storyViewModel.getStories().single()
        assertNotNull(result)

        val resultList = result.collectData()

        println("Result list size: ${resultList.size}")

        assertEquals(0, resultList.size)
    }
}
