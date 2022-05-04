package com.yks.videogamesapp.service

import com.yks.videogamesapp.model.GameDetails
import com.yks.videogamesapp.model.Games
import com.yks.videogamesapp.utils.Constants.GAME_DETAILS
import com.yks.videogamesapp.utils.Constants.GAME_LIST
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(GAME_LIST)
    fun getGames(
        @Query("page")page: Int
    ): Single<Games>

    @GET(GAME_DETAILS)
    fun getGameDetails(
        @Path("id")id: String
    ): Single<GameDetails>

}