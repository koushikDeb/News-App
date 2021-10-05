package com.example.assignmentnewsapp.networking.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.networking.model.Response
import com.example.assignmentnewsapp.repo.NewsRepo

class AllNewsPostDataSource(private val newsRepo: NewsRepo,
    private val query:String, private val fromDate:String, private val toDate:String) : PagingSource<Int, Article>() {
    lateinit var response: Response
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            val currentLoadingPageKey = params.key ?: 1
             response = newsRepo.getNewsFromRepo(query,fromDate,toDate,currentLoadingPageKey)


            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = response.articles,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

}