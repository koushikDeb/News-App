package com.example.assignmentnewsapp.repo

import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.networking.retrofit.ApiService
import javax.inject.Inject

class NewsRepo @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getNewsFromRepo() = apiHelper.getNews()
}