package com.yks.videogamesapp.repo.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.service.ApiService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesPagingSource @Inject constructor(
    private val apiService: ApiService
): RxPagingSource<Int, GamesResult>() {
    override fun getRefreshKey(state: PagingState<Int, GamesResult>): Int? {
        return null
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GamesResult>> {
        val page = params.key ?: 1
        return apiService.getGames(page)
            .subscribeOn(Schedulers.io())
            .map { games ->
                games?.let {
                    LoadResult.Page(
                        data = it.gamesResult!!,
                        prevKey = if (it.previous == null) null else page-1,
                        nextKey = if (it.next == null) null else page+1
                    ) as LoadResult<Int,GamesResult>
                }
            }.onErrorReturn {
                LoadResult.Error(it)
            }
    }

}