package com.yks.videogamesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class LikedGames (
    @PrimaryKey
    val id: Int,
    val time: Long?,
    val name: String, // -> non empty
    val released: String? = "",
    val rating: Double? = 0.0,
    @SerializedName("background_image")
    var backgroundImage: String? = ""
)