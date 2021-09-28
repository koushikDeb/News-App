package com.example.assignmentnewsapp.repo

import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.networking.retrofit.ApiService

class NewsRepo(private val apiHelper: ApiHelper) {
    suspend fun getNewsFromRepo() = apiHelper.getNews()
}