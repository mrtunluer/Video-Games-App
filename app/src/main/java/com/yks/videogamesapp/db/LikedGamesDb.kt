package com.yks.videogamesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yks.videogamesapp.model.LikedGames

@Database(
    entities = [LikedGames::class],
    version = 1
)
abstract class LikedGamesDb: RoomDatabase() {
    companion object{
        const val DB_NAME = "liked_games_db"
    }
    abstract fun getLikedGamesDao(): LikedGamesDao
}