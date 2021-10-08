package com.example.assignmentnewsapp.repo.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignmentnewsapp.networking.model.Article

@Dao
interface AllNewsModelDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(newsArticle: List<Article>)

  @Query("SELECT * FROM Article")
  fun getAllNewsArticles(): PagingSource<Int, Article>

  @Query("DELETE FROM Article")
  suspend fun clearAllNews()

  @Query("SELECT * FROM Article")
  suspend fun getAll():List<Article>
}