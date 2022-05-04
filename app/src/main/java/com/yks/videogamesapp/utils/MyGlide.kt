package com.yks.videogamesapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yks.videogamesapp.R

fun ImageView.download(context: Context, uri: String?) {
    Glide.with(context)
        .load(uri)
        .placeholder(R.drawable.placeholder)
        .into(this)
}