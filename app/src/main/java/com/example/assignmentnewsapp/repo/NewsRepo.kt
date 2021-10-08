package com.example.assignmentnewsapp.repo

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.repo.local.AppDatabase
import com.vikas.paging3.data.AllNewsMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//,private val appDatabase:AppDatabase
class NewsRepo @Inject constructor(private val apiHelper: ApiHelper,private val appDatabase:AppDatabase) {
  suspend fun getNewsFromRepo(
    query: String,
    fromDate: String,
    toDate: String,
    page: Int,
    pageSize:Int
  ) = apiHelper.getNews(query, fromDate, toDate, page,pageSize)

  companion object{val DEFAULT_PAGE_SIZE = 5}

  // suspend fun getArticlesFromRepo(
  //   query: String,
  //   fromDate: String,
  //   toDate: String,
  //   page: Int
  // ) = apiHelper.getNews(query, fromDate, toDate, page).articles


  @ExperimentalPagingApi fun getNewsStremMed(query: String,
    fromDate: String,
    toDate: String): Flow<PagingData<Article>> {

    if (appDatabase == null) throw IllegalStateException("Database is not initialized")

    val pagingSourceFactory = { appDatabase.getAllNewsModelDao().getAllNewsArticles() }
   // val pagingSourceFactory = { AllNewsPostDataSource(NewsRepo(apiHelper,appDatabase), query, fromDate, toDate) }
    return Pager(
      config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
      pagingSourceFactory = pagingSourceFactory,
      remoteMediator = AllNewsMediator(appDatabase,apiHelper,query,fromDate,toDate)
    ).flow
  }






  // fun getNewsStremMed(
  //   query: String,
  //   fromDate: String,
  //   toDate: String
  // ): Flow<PagingData<Article>> {
  //   var pagerData: Flow<PagingData<Article>> =
  //     Pager(config = PagingConfig(pageSize = 5, enablePlaceholders = false),
  //       pagingSourceFactory = {
  //         AllNewsPostDataSource(NewsRepo(apiHelper), query, fromDate, toDate)
  //       }).flow
  //   return pagerData
  // }
}