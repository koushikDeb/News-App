package com.example.assignmentnewsapp.di.module

import android.content.Context
import androidx.room.Room

import com.example.assignmentnewsapp.networking.retrofit.ApiService
import com.example.assignmentnewsapp.networking.retrofit.NewsArticleModifierInterceptor
import com.example.assignmentnewsapp.repo.local.allNewsDaos.AppDatabase
import com.example.assignmentnewsapp.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
  @Provides
  @Singleton
  fun provideOkHttpClient(
    modifierInterceptor: NewsArticleModifierInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .addInterceptor(modifierInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideNewsArticleModifierInterceptor(): NewsArticleModifierInterceptor {
    return NewsArticleModifierInterceptor()
  }

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor()
  }

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
  ): AppDatabase {
    return Room.databaseBuilder(
      app,
      AppDatabase::class.java,
      "news.db"
    ).build()
  }

  @Provides
  @Singleton
  fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}