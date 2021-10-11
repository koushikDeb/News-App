package com.example.assignmentnewsapp.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData

import com.example.assignmentnewsapp.data.model.allnews.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.repo.local.allNewsDaos.AppDatabase
import com.example.assignmentnewsapp.ui.paging.dataSource.allnewsDataSource.AllNewsMediator
import com.example.assignmentnewsapp.utils.Constants.Companion.TOPIC_EVERYTHING
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//,private val appDatabase:AppDatabase
class NewsRepo @Inject constructor(
  private val apiHelper: ApiHelper,
  private val appDatabase: AppDatabase
) {
  suspend fun getNewsFromRepo(
    query: String,
    fromDate: String,
    toDate: String,
    page: Int,
    pageSize: Int
  ) = apiHelper.getNews(query, fromDate, toDate, page, pageSize)

  companion object {
    const val DEFAULT_PAGE_SIZE = 5
  }

  @ExperimentalPagingApi fun getNewsStremMed(
    query: String,
    fromDate: String,
    toDate: String
  ): Flow<PagingData<Article>> {

    if (appDatabase == null) throw IllegalStateException("Database is not initialized")

    val pagingSourceFactory =
      { appDatabase.getAllNewsModelDao().getAllNewsArticles(TOPIC_EVERYTHING) }
    // val pagingSourceFactory = { AllNewsPostDataSource(NewsRepo(apiHelper,appDatabase), query, fromDate, toDate) }
    return Pager(
      config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
      pagingSourceFactory = pagingSourceFactory,
      remoteMediator = AllNewsMediator(apiHelper, query, fromDate, toDate, appDatabase)
    ).flow
  }
}