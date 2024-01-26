package ru.goaliepash.playlistmaker.presentation.state

import ru.goaliepash.domain.model.Playlist

sealed interface PlaylistsState {

    object Loading : PlaylistsState

    data class Content(val playlists: List<Playlist>) : PlaylistsState

    object Empty : PlaylistsState
}