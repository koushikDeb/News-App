package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.utils.Constants
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {
    suspend fun getNews() = apiService.getNews("apple","2021-09-27","2021-09-2",Constants.NEWS_API)
}