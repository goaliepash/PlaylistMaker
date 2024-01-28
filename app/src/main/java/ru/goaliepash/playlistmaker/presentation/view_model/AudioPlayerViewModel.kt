package ru.goaliepash.playlistmaker.presentation.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.goaliepash.domain.interactor.FavoriteTracksInteractor
import ru.goaliepash.domain.interactor.PlaylistsInteractor
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.presentation.state.AudioPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val audioPlayerState = MutableLiveData<AudioPlayerState>(AudioPlayerState.Default())
    private val isExistsInFavorites = MutableLiveData<Boolean>()
    private val playlists = MutableLiveData<List<Playlist>>()
    private val isTrackAddedState = MutableLiveData<Boolean>()

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var timerJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
        timerJob?.cancel()
    }

    fun getAudioPlayerState(): LiveData<AudioPlayerState> = audioPlayerState

    fun getExistsInFavorites(): LiveData<Boolean> = isExistsInFavorites

    fun getPlaylistsState(): LiveData<List<Playlist>> = playlists

    fun getTrackAddedState(): LiveData<Boolean> = isTrackAddedState

    fun initMediaPlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            audioPlayerState.postValue(AudioPlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            audioPlayerState.postValue(AudioPlayerState.Prepared())
        }
    }

    fun onPause() {
        pausePlayer()
    }

    fun onImageButtonPlayPauseClicked() {
        when (audioPlayerState.value) {
            is AudioPlayerState.Playing -> {
                pausePlayer()
            }

            is AudioPlayerState.Prepared, is AudioPlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    fun onImageButtonLikeClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteTracksInteractor.deleteFavoriteTrack(track)
                isExistsInFavorites.postValue(false)
            } else {
                favoriteTracksInteractor.addFavoriteTrack(track)
                isExistsInFavorites.postValue(true)
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            getPlaylistsFromDb()
        }
    }

    fun updatePlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            if (playlist.trackIds.contains(track.trackId)) {
                isTrackAddedState.postValue(false)
            } else {
                playlistsInteractor.addToPlaylist(track)
                updatePlaylist(
                    playlist.trackIds,
                    track.trackId,
                    playlist.tracksCount,
                    playlist.dateAdded
                )
                getPlaylistsFromDb()
                isTrackAddedState.postValue(true)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        audioPlayerState.postValue(AudioPlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        audioPlayerState.postValue(AudioPlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        audioPlayerState.value = AudioPlayerState.Default()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(UPDATE_TIME_DELAY_MILLIS)
                audioPlayerState.postValue(AudioPlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat(
            TIME_FORMAT,
            Locale.getDefault()
        ).format(mediaPlayer.currentPosition) ?: START_TIME
    }

    private suspend fun getPlaylistsFromDb() {
        playlistsInteractor
            .getPlaylists()
            .collect {
                if (it.isNotEmpty()) {
                    playlists.postValue(it)
                }
            }
    }

    private suspend fun updatePlaylist(
        trackIds: List<String>,
        trackId: String,
        tracksCount: Int,
        dateAdded: Long
    ) {
        val newTrackIds = mutableListOf<String>()
        newTrackIds.addAll(trackIds)
        newTrackIds.add(trackId)
        val newTracksCount = tracksCount + 1
        playlistsInteractor.updatePlaylist(newTrackIds, newTracksCount, dateAdded)
    }

    companion object {
        private const val TIME_FORMAT = "mm:ss"
        private const val START_TIME = "00:00"
        private const val UPDATE_TIME_DELAY_MILLIS = 300L
    }
}