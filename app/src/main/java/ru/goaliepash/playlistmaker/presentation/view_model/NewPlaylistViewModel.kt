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
    private val isPlaylistUpdated = MutableLiveData(false)

    fun isPlaylistCreated(): LiveData<Boolean> = isPlaylistCreated

    fun isPlaylistUpdated(): LiveData<Boolean> = isPlaylistUpdated

    fun onButtonSaveClicked(id: Int, name: String, description: String, coverUri: String) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylistInfo(
                id = id,
                name = name,
                description = description,
                coverUri = coverUri
            )
            isPlaylistUpdated.postValue(true)
        }
    }

    fun onButtonCreateClicked(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlaylists(playlist)
            isPlaylistCreated.postValue(true)
        }
    }
}