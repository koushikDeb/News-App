package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.networking.model.Response
import com.example.assignmentnewsapp.utils.Constants.Companion.TOPIC_EVERYTHING
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
  @GET(TOPIC_EVERYTHING)
  suspend fun getNews(
    @Query("q") query: String,
    @Query("from") fromDate: String,
    @Query("to") toDate: String,
    @Query("apiKey") apikey: String,
    @Query("page") page: Int,
    @Query("pageSize") pagesize: Int
  ): Response
}