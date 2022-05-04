package com.yks.videogamesapp.model

import com.google.gson.annotations.SerializedName

data class Games (
    val page: Int?,
    val next: String?,
    val previous: String?,
    @SerializedName("results")
    val gamesResult: ArrayList<GamesResult>?
    )