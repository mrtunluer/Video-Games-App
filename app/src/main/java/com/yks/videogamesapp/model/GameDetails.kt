package com.yks.videogamesapp.model

import com.google.gson.annotations.SerializedName

data class GameDetails (
    val id: Int? = 0,
    val slug: String, // -> non empty
    val name: String, // -> non empty
    val released: String? = "",
    val metacritic: Int? = 0,
    val rating: Double? = 0.0,
    var description: String? = "",
    @SerializedName("background_image")
    var backgroundImage: String? = ""
)