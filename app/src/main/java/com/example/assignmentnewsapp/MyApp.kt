package com.example.assignmentnewsapp

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class MyApp : Application() {
  companion object {
    lateinit var res: Resources
  }

  override fun onCreate() {
    super.onCreate()
    res = resources
    Timber.plant(DebugTree())
  }
}