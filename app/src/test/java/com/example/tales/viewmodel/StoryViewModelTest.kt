package com.example.tales.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.tales.collectData
import com.example.tales.data.FakeStoryRepository
import com.example.tales.model.Story
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
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
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
        Dispatchers.resetMain()
        dispatcher.cancel()
    }

    @Test
    fun `when getStories called with data then return data`() = runTest(timeout = 30.seconds) {
        println("Starting test: when getStories called with data then return data")
        val result = storyViewModel.getStories().single()
        assertNotNull(result)
        println("Result is not null")

        val resultList = result.collectData()
        println("Data collected from PagingData")

        println("Result list size: ${resultList.size}")
        resultList.forEachIndexed { index, story ->
            println("Story $index: ${story.id}")
        }

        assertEquals(2, resultList.size)
        assertEquals("1", resultList[0].id)
    }

    @Test
    fun `when getStories called with no data then return empty`() = runTest(timeout = 30.seconds) {
        println("Starting test: when getStories called with no data then return empty")
        val emptyFlow = flowOf(PagingData.empty<Story>())
        fakeRepository.overrideGetStoriesStream(emptyFlow)

        val result = storyViewModel.getStories().single()
        assertNotNull(result)
        println("Result is not null")

        val resultList = result.collectData()
        println("Data collected from PagingData")

        println("Result list size: ${resultList.size}")

        assertEquals(0, resultList.size)
    }
}
