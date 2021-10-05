package com.example.assignmentnewsapp.repo

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.assignmentnewsapp.networking.dataSource.AllNewsPostDataSource
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepo @Inject constructor(private val apiHelper: ApiHelper) {
  suspend fun getNewsFromRepo(query:String, fromDate:String, toDate:String,page:Int) = apiHelper.getNews(query,fromDate,toDate,page)


  fun getNewsStream(query:String, fromDate:String, toDate:String): Flow<PagingData<Article>> {
    var pagerData: Flow<PagingData<Article>> = Pager (config = PagingConfig(pageSize = 5,enablePlaceholders = false),
      pagingSourceFactory = {
        AllNewsPostDataSource(NewsRepo(apiHelper),query,fromDate,toDate)
      }).flow
    return pagerData
  }
}