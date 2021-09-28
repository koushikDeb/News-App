package com.example.assignmentnewsapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.ui.fragments.allnews.AllNewsViewModel


//USED IN PREVIOUS VERSION "NEWS APP WITHOUT HILT"
//NOT IN USE AS HILT PROVIDING THE VIEW MODEL INJECTION

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