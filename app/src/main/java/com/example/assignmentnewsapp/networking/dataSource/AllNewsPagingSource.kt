package com.vikas.paging3.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.utils.Constants.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class AllNewsPagingSource(
    val apiHelper: ApiHelper,
    val query:String,
    val fromDate:String,
    val todate:String
) :
    PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiHelper.getNews(query,fromDate,todate,page, params.loadSize)
            LoadResult.Page(
                response, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
       return state.anchorPosition
    }
}