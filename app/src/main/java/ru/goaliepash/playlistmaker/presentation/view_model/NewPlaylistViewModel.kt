package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.goaliepash.domain.interactor.PlaylistsInteractor
import ru.goaliepash.domain.model.Playlist

class NewPlaylistViewModel(private val playlistInteractor: PlaylistsInteractor) : ViewModel() {

    private val isPlaylistCreated = MutableLiveData(false)

    fun isPlaylistCreated(): LiveData<Boolean> = isPlaylistCreated

    fun onButtonCreateClicked(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlaylists(playlist)
            isPlaylistCreated.postValue(true)
        }
    }
}