package com.yks.videogamesapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.yks.videogamesapp.model.LikedGames
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedGamesDao {

    @Query("SELECT * FROM LikedGames ORDER BY time desc")
    fun getAllLikedGames(): PagingSource<Int, LikedGames>

    @Query("SELECT * FROM LikedGames where id = :id")
    fun getLikedGame(id: Int): Flow<LikedGames?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedGame(gamesResult: LikedGames)

    @Query("DELETE FROM LikedGames where id = :id")
    suspend fun deleteLikedGame(id: Int)

}