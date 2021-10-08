package com.example.assignmentnewsapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignmentnewsapp.MyApp

open abstract class BaseViewModel : ViewModel() {
  val commonMessageChannel = MutableLiveData<String>()
  val _showProgressBar = MutableLiveData<Boolean>()

  fun getStringRes(id: Int) = MyApp.res.getString(id)
}