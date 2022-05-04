package com.yks.videogamesapp.utils

import androidx.paging.PagingConfig
import com.yks.videogamesapp.utils.Constants.PAGE_SIZE
import com.yks.videogamesapp.utils.Constants.PREFETCH_DISTANCE

fun pagingConfig(): PagingConfig{
    return PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
        prefetchDistance = PREFETCH_DISTANCE
    )
}