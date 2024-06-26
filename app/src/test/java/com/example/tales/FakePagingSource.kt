package com.example.tales

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tales.model.Story

class FakePagingSource(private val data: List<Story>) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        println("FakePagingSource: getRefreshKey called")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        println("FakePagingSource: load called with params: $params")
        val page = params.key ?: 1
        return LoadResult.Page(
            data = data,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (data.isEmpty()) null else page + 1
        ).also {
            println("FakePagingSource: LoadResult.Page created with data size: ${data.size}")
        }
    }
}
