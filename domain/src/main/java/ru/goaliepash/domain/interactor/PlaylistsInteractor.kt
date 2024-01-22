package ru.goaliepash.domain.interactor

import ru.goaliepash.domain.model.Playlist

interface PlaylistsInteractor {

    suspend fun addPlaylists(playlist: Playlist)
}