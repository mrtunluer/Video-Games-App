package com.yks.videogamesapp.repo

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.repo.paging.GamesPagingSource
import com.yks.videogamesapp.utils.pagingConfig
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GamesRepo @Inject constructor(private val gamesPagingSource: GamesPagingSource) {
    fun getGamesResult(): Flowable<PagingData<GamesResult>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = {gamesPagingSource}
        ).flowable
    }
}
