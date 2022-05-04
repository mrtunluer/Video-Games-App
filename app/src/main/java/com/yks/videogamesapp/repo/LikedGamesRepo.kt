package com.yks.videogamesapp.repo

import com.yks.videogamesapp.db.LikedGamesDb
import com.yks.videogamesapp.model.LikedGames
import javax.inject.Inject

class LikedGamesRepo @Inject constructor(private val db: LikedGamesDb){

    fun getAllLikedGames() = db.getLikedGamesDao().getAllLikedGames()

    fun getLikedGame(id: Int) = db.getLikedGamesDao().getLikedGame(id)

    suspend fun insertLikedGame(likedGame: LikedGames) =  db.getLikedGamesDao().insertLikedGame(likedGame)

    suspend fun deleteLikedGame(id: Int) = db.getLikedGamesDao().deleteLikedGame(id)

}