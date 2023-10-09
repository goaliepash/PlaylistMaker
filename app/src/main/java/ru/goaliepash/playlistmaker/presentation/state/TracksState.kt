package ru.goaliepash.playlistmaker.presentation.state

import ru.goaliepash.playlistmaker.domain.model.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(val tracks: List<Track>) : TracksState

    object Empty : TracksState

    object Error : TracksState
}