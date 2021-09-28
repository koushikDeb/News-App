package com.example.assignmentnewsapp.networking.retrofit

import com.example.assignmentnewsapp.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//USED IN PREVIOUS VERSION "NEWS APP WITHOUT HILT"
//NOT IN USE AS HILT PROVIDING THE DEPENDENCY FOR RetrofitBuilder
object RetrofitBuilder {
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}