package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.goaliepash.domain.interactor.PlaylistsInteractor
import ru.goaliepash.playlistmaker.presentation.state.PlaylistsState

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val playlistsState = MutableLiveData<PlaylistsState>()

    fun getPlaylistsState(): LiveData<PlaylistsState> = playlistsState

    fun getPlaylists() {
        viewModelScope.launch {
            renderPlaylistsState(PlaylistsState.Loading)
            playlistsInteractor
                .getPlaylists()
                .collect {
                    if (it.isEmpty()) {
                        renderPlaylistsState(PlaylistsState.Empty)
                    } else {
                        renderPlaylistsState(PlaylistsState.Content(it))
                    }
                }
        }
    }

    private fun renderPlaylistsState(state: PlaylistsState) {
        playlistsState.value = state
    }
}