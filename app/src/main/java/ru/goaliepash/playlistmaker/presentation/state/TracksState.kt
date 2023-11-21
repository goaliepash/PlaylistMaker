package ru.goaliepash.playlistmaker.presentation.state

import ru.goaliepash.domain.model.Track

sealed interface TracksState {

    object Loading : TracksState

    data class Content(val tracks: List<Track>) : TracksState

    object Empty : TracksState

    object Error : TracksState

    object Cancel : TracksState
}