package com.example.assignmentnewsapp.ui.fragments.allnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.assignmentnewsapp.R.string
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.repo.NewsRepo
import com.example.assignmentnewsapp.repo.local.AppDatabase
import com.example.assignmentnewsapp.ui.base.BaseViewModel
import com.example.assignmentnewsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(private val apiHelper: ApiHelper,private val  appDatabase: AppDatabase) : BaseViewModel() {

  // val showProgressBar: LiveData<Boolean>
  //   get() = _showProgressBar
  //
  // fun getNews(query:String, fromDate:String, toDate:String) = liveData(Dispatchers.IO) {
  //   emit(Resource.loading(data = getStringRes(string.Status_Loading)))
  //   _showProgressBar.postValue(true)
  //   try {
  //     emit(Resource.success(data = NewsRepo(apiHelper).getNewsFromRepo(query,fromDate,toDate,1)))
  //     _showProgressBar.postValue(false)
  //   } catch (exception: Exception) {
  //     emit(Resource.error(data = null, message = exception.message ?: getStringRes(string.Status_Error)))
  //     _showProgressBar.postValue(false)
  //     commonMessageChannel.postValue(exception.message ?: getStringRes(string.Status_Error))
  //   }
  // }

  @ExperimentalPagingApi
  fun getListData(
    query: String,
    fromDate: String,
    toDate: String
  ): Flow<PagingData<Article>> {
    return NewsRepo(apiHelper,appDatabase).getNewsStremMed(query, fromDate, toDate).cachedIn(viewModelScope)
  }
}
