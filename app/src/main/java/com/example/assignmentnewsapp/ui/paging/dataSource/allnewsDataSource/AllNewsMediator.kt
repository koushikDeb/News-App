/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.assignmentnewsapp.ui.paging.dataSource.allnewsDataSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.assignmentnewsapp.data.model.allnews.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.repo.local.allNewsDaos.AppDatabase
import com.example.assignmentnewsapp.repo.local.allNewsDaos.RemoteKeys
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class AllNewsMediator(
  val apiHelper: ApiHelper,
  val query: String,
  val fromDate: String,
  val todate: String,
  private val repoDatabase: AppDatabase
) : RemoteMediator<Int, Article>() {

  override suspend fun initialize(): InitializeAction {
    // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
    // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
    // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
    // triggering remote refresh.
    return InitializeAction.LAUNCH_INITIAL_REFRESH
  }

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, Article>
  ): MediatorResult {

    val page = when (loadType) {
      LoadType.REFRESH -> {
        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
        remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
      }
      LoadType.PREPEND -> {
        val remoteKeys = getRemoteKeyForFirstItem(state)
        // If remoteKeys is null, that means the refresh result is not in the database yet.
        // We can return Success with `endOfPaginationReached = false` because Paging
        // will call this method again if RemoteKeys becomes non-null.
        // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
        // the end of pagination for prepend.
        val prevKey = remoteKeys?.prevKey
        if (prevKey == null) {
          return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        }
        prevKey
      }
      LoadType.APPEND -> {
        val remoteKeys = getRemoteKeyForLastItem(state)
        // If remoteKeys is null, that means the refresh result is not in the database yet.
        // We can return Success with `endOfPaginationReached = false` because Paging
        // will call this method again if RemoteKeys becomes non-null.
        // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
        // the end of pagination for append.
        val nextKey = remoteKeys?.nextKey
        if (nextKey == null) {
          return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        }
        nextKey
      }
    }



    try {
      val apiResponse = apiHelper.getNews(query, fromDate, todate, page, state.config.pageSize)
      Timber.e("====$page======${state.config.pageSize}")
      val news = apiResponse
      val endOfPaginationReached = news.isEmpty()
      repoDatabase.withTransaction {
        // clear all tables in the database
        if (loadType == LoadType.REFRESH) {
          repoDatabase.getRepoDao().clearRemoteKeys()
          repoDatabase.getAllNewsModelDao().clearAllNews()
        }
        val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        repoDatabase.getAllNewsModelDao().insertAll(news)

        val keys = repoDatabase.getAllNewsModelDao().getAll().map {
          RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
        }
        repoDatabase.getRepoDao().insertAll(keys)

      }
      return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (exception: IOException) {
      return MediatorResult.Error(exception)
    } catch (exception: HttpException) {
      return MediatorResult.Error(exception)
    }
  }

  private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
    // Get the last page that was retrieved, that contained items.
    // From that last page, get the last item
    return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
      ?.let { repo ->
        // Get the remote keys of the last item retrieved
        repoDatabase.getRepoDao().remoteKeysRepoId(repo.id)
      }
  }

  private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
    // Get the first page that was retrieved, that contained items.
    // From that first page, get the first item
    return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
      ?.let { repo ->
        // Get the remote keys of the first items retrieved
        repoDatabase.getRepoDao().remoteKeysRepoId(repo.id)
      }
  }

  private suspend fun getRemoteKeyClosestToCurrentPosition(
    state: PagingState<Int, Article>
  ): RemoteKeys? {
    // The paging library is trying to load data after the anchor position
    // Get the item closest to the anchor position
    return state.anchorPosition?.let { position ->
      state.closestItemToPosition(position)?.id?.let { repoId ->
        repoDatabase.getRepoDao().remoteKeysRepoId(repoId)
      }
    }
  }
}
