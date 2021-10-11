package com.example.assignmentnewsapp.repo.local.allNewsDaos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.assignmentnewsapp.data.model.allnews.Article
import com.example.assignmentnewsapp.utils.RoomTypeConverters

@Database(version = 1, entities = [Article::class, RemoteKeys::class], exportSchema = false)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun getRepoDao(): RemoteKeysDao
  abstract fun getAllNewsModelDao(): AllNewsModelDao

  companion object {

    val NEWS_DB = "news.db"

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase =
      INSTANCE ?: synchronized(this) {
        INSTANCE
          ?: buildDatabase(context).also { INSTANCE = it }
      }

    private fun buildDatabase(context: Context) =
      Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, NEWS_DB)
        .build()
  }
}