package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.utils.Constants
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {
  suspend fun getNews(query:String, fromDate:String, toDate:String, page:Int) = apiService.getNews(query, fromDate, toDate, Constants.NEWS_API,page)
}