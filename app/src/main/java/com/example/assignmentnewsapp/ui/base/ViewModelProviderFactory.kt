package com.example.assignmentnewsapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.ui.fragments.allnews.AllNewsViewModel

class ViewModelProviderFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when
        {
            modelClass.isAssignableFrom(AllNewsViewModel::class.java)->return AllNewsViewModel(apiHelper) as T
            modelClass.isAssignableFrom(AllNewsViewModel::class.java)->return AllNewsViewModel(apiHelper) as T
            modelClass.isAssignableFrom(AllNewsViewModel::class.java)->return AllNewsViewModel(apiHelper) as T
        }
        if (modelClass.isAssignableFrom(AllNewsViewModel::class.java)) {
            return AllNewsViewModel(apiHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}