package com.example.tales

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
    val items = mutableListOf<T>()
    val flow = flowOf(this)
    flow.collectLatest { pagingData ->
        pagingData.map { item ->
            println("Collecting item: $item")
            items.add(item)
        }
    }
    return items
}
