package com.example.tales

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
    val items = mutableListOf<T>()
    val flow = flowOf(this)
    println("collectData: starting to collect from PagingData")
    flow.collectLatest { pagingData ->
        pagingData.map { item ->
            println("collectData: item collected: $item")
            items.add(item)
        }
    }
    println("collectData: finished collecting from PagingData with items: ${items.size}")
    return items
}
