package com.yks.videogamesapp.model

import com.google.gson.annotations.SerializedName

data class GamesResult (
        val id: Int? = 0,
        val slug: String, // -> non empty
        val name: String, // -> non empty
        val released: String? = "",
        val rating: Double? = 0.0,
        @SerializedName("background_image")
        var backgroundImage: String? = ""
        )