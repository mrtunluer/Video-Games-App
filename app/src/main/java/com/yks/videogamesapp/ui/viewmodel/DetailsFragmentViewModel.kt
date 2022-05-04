package com.yks.videogamesapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yks.videogamesapp.model.GameDetails
import com.yks.videogamesapp.model.LikedGames
import com.yks.videogamesapp.repo.DetailRepo
import com.yks.videogamesapp.repo.LikedGamesRepo
import com.yks.videogamesapp.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val detailRepo: DetailRepo,
    private val likedGamesRepo: LikedGamesRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val gameDetails: MutableLiveData<DataStatus<GameDetails>> by lazy {
        MutableLiveData<DataStatus<GameDetails>>()
    }

    private val _state = MutableStateFlow<DataStatus<LikedGames>>(DataStatus.Loading())
    val likeBtnState = _state.asStateFlow()

    val id = savedStateHandle.get<Int>("id")

    init {
        getGameDetails()
        isLikedGame()
    }

    fun getGameDetails(){
        compositeDisposable.addAll(
            id?.let {
                detailRepo.getGameDetails(it)
                    .doOnSubscribe{gameDetails.value = DataStatus.Loading()}
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {details -> gameDetails.value = DataStatus.Success(details)},
                        {error -> gameDetails.value = DataStatus.Error(error.message)}
                    )
            }
        )
    }

    fun deleteLikeGame(){
        viewModelScope.launch {
            id?.let {
                likedGamesRepo.deleteLikedGame(id)
            }
        }
    }

    fun insertLikedGame(likedGame: LikedGames) {
        viewModelScope.launch {
            id?.let {
                likedGamesRepo.insertLikedGame(likedGame)
            }
        }
    }

    private fun isLikedGame() {
        viewModelScope.launch {
            id?.let {
                likedGamesRepo.getLikedGame(id).collect { isLikedGame ->
                    if (isLikedGame != null){
                        _state.value = DataStatus.Success(isLikedGame)
                    }else{
                        _state.value = DataStatus.Empty()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}