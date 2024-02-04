package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import ru.goaliepash.domain.interactor.PlaylistsInteractor
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val playlist = MutableLiveData<Playlist?>()
    private val playlistTracks = MutableLiveData<List<Track>>()

    fun getPlaylist(): LiveData<Playlist?> = playlist

    fun getPlaylistTracks(): LiveData<List<Track>> = playlistTracks

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylist(id).collect {
                playlist.postValue(it)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPlaylistTracks(id: Int) {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylist(id)
                .flatMapConcat {
                    playlistsInteractor.getPlaylistTracks(it.trackIds)
                }
                .collect {
                    playlistTracks.postValue(it)
                }
        }
    }

    fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor
                .deleteTrackFromPlaylist(trackId, playlistId)
                .collect {
                    val newPlaylistTracks = mutableListOf<Track>()
                    newPlaylistTracks.addAll(
                        playlistTracks
                            .value
                            ?.filter {
                                it.trackId != trackId
                            }
                            .orEmpty()
                    )
                    playlistTracks.postValue(newPlaylistTracks)
                    playlist.postValue(it)
                }
        }
    }

    fun deletePlaylist(currentPlaylist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(currentPlaylist)
            playlist.postValue(null)
        }
    }
}