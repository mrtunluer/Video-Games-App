package com.yks.videogamesapp.repo

import com.yks.videogamesapp.model.GameDetails
import com.yks.videogamesapp.service.ApiService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DetailRepo @Inject constructor(private val apiService: ApiService) {
    fun getGameDetails(movieId: Int): Single<GameDetails> {
        return apiService.getGameDetails(movieId.toString())
            .subscribeOn(Schedulers.io())
    }
}