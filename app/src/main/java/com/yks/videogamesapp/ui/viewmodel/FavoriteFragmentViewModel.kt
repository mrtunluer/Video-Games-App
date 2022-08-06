package com.yks.videogamesapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yks.videogamesapp.model.LikedGames
import com.yks.videogamesapp.repo.LikedGamesRepo
import com.yks.videogamesapp.utils.pagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(
    private val likedGamesRepo: LikedGamesRepo,
) : ViewModel() {

    val likedGamesLiveData: MutableLiveData<PagingData<LikedGames>> by lazy {
        MutableLiveData<PagingData<LikedGames>>()
    }

    init {
        collectData()
    }

    private fun getAllLikedGames(): Flow<PagingData<LikedGames>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = { likedGamesRepo.getAllLikedGames() }
        ).flow.cachedIn(viewModelScope)
    }

    private fun collectData() {
        viewModelScope.launch {
            getAllLikedGames().distinctUntilChanged().collectLatest { likedGames ->
                likedGamesLiveData.value = likedGames
            }
        }
    }

}