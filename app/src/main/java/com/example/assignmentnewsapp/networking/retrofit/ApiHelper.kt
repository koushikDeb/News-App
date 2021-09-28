package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.utils.Constants

class ApiHelper(private val apiService: ApiService) {
    suspend fun getNews() = apiService.getNews("apple","2021-09-27","2021-09-2",Constants.NEWS_API)
}