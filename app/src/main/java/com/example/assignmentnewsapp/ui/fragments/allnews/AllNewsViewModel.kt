package com.example.assignmentnewsapp.ui.fragments.allnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.repo.NewsRepo
import com.example.assignmentnewsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(private val apiHelper: ApiHelper) : ViewModel() {

  fun getNews() = liveData(Dispatchers.IO) {
    emit(Resource.loading(data = "loading..."))
    try {
      emit(Resource.success(data = NewsRepo(apiHelper).getNewsFromRepo()))
    } catch (exception: Exception) {
      emit(Resource.error(data = null, message = exception.message ?: "Error!"))
    }
  }
}