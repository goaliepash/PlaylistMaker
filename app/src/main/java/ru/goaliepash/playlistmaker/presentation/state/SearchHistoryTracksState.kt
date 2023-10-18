package ru.goaliepash.playlistmaker.presentation.state

import ru.goaliepash.domain.model.Track

sealed interface SearchHistoryTracksState {

    data class Addition(val tracks: List<Track>) : SearchHistoryTracksState

    data class Receipt(val tracks: List<Track>) : SearchHistoryTracksState

    data class Cleaning(val tracks: List<Track>) : SearchHistoryTracksState
}