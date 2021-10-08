package com.vikas.paging3.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.repo.local.AppDatabase
import com.example.assignmentnewsapp.repo.local.RemoteKeys
import com.example.assignmentnewsapp.utils.Constants.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import java.util.Arrays

@ExperimentalPagingApi
class AllNewsMediator(
    val appDatabase: AppDatabase,
    val apiHelper: ApiHelper,
    val query:String,
    val fromDate:String,
    val todate:String) :
    RemoteMediator<Int, Article>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Article>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = apiHelper.getNews(query,fromDate,todate,page, state.config.pageSize)
            val isEndOfList = response.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
                    appDatabase.getAllNewsModelDao().clearAllNews()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                appDatabase.getAllNewsModelDao().insertAll(response)
                val keys = appDatabase.getAllNewsModelDao().getAll().map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.getRepoDao().insertAll(keys)
                //appDatabase.getAllNewsModelDao().insertAll(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Article>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages
            .lastOrNull {
                it.data.isNotEmpty()
            }
            ?.data?.lastOrNull()
            ?.let {
                news -> appDatabase.getRepoDao().remoteKeysRepoId(news.id)
            }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Article>): RemoteKeys? {
        val allkey= Arrays.deepToString(arrayOf(appDatabase.getRepoDao().getAllKey()))
        Timber.e(allkey)
        return state.pages
            .firstOrNull() {
                it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { news -> appDatabase.getRepoDao().remoteKeysRepoId(news.id)
            }
    }



    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, Article>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.getRepoDao().remoteKeysRepoId(repoId)
            }
        }
    }

}