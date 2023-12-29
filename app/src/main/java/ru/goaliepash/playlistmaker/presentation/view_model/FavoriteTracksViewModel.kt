package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.goaliepash.domain.interactor.FavoriteTracksInteractor
import ru.goaliepash.playlistmaker.presentation.state.FavoriteTracksState

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) : ViewModel() {

    private val favoriteTracksState = MutableLiveData<FavoriteTracksState>()

    fun getFavoriteTracksState(): LiveData<FavoriteTracksState> = favoriteTracksState

    fun getFavoriteTracks() {
        viewModelScope.launch {
            renderFavoriteTracksState(FavoriteTracksState.Loading)
            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect {
                    if (it.isEmpty()) {
                        renderFavoriteTracksState(FavoriteTracksState.Empty)
                    } else {
                        renderFavoriteTracksState(FavoriteTracksState.Content(it.reversed()))
                    }
                }
        }
    }

    private fun renderFavoriteTracksState(state: FavoriteTracksState) {
        favoriteTracksState.value = state
    }
}