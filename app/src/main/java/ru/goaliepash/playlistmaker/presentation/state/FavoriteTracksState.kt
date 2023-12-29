package ru.goaliepash.playlistmaker.presentation.state

import ru.goaliepash.domain.model.Track

sealed interface FavoriteTracksState {

    object Loading : FavoriteTracksState

    data class Content(val favoriteTracks: List<Track>) : FavoriteTracksState

    object Empty : FavoriteTracksState
}