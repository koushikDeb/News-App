package com.example.assignmentnewsapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.assignmentnewsapp.BuildConfig
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.networking.retrofit.ApiService
import com.example.assignmentnewsapp.networking.retrofit.NewsArticleModifierInterceptor
import com.example.assignmentnewsapp.repo.local.AppDatabase
import com.example.assignmentnewsapp.repo.local.AppDatabase.Companion
import com.example.assignmentnewsapp.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
  @Provides
  @Singleton
  fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    val modifierInterceptor = NewsArticleModifierInterceptor()
    loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
    OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)

      .build()
  } else OkHttpClient
    .Builder()
    .build()

  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .build()



  @Singleton
  @Provides
  fun provideYourDatabase(
    @ApplicationContext app: Context
  ) = Room.databaseBuilder(
    app,
    AppDatabase::class.java,
    "news.db"
  ).build()


  @Provides
  @Singleton
  fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}