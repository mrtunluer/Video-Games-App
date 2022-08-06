package com.yks.videogamesapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.yks.videogamesapp.model.GamesResult
import com.yks.videogamesapp.repo.GamesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repo: GamesRepo
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val gamesResult: MutableLiveData<PagingData<GamesResult>> by lazy {
        MutableLiveData<PagingData<GamesResult>>()
    }

    init {
        getGamesResult()
    }

    fun getGamesResult() {
        compositeDisposable.add(
            repo.getGamesResult()
                .cachedIn(viewModelScope)
                .subscribe { result ->
                    gamesResult.value = result
                }
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}