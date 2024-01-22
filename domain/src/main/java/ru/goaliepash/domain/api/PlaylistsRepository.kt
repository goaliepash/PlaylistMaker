package ru.goaliepash.domain.api

import ru.goaliepash.domain.model.Playlist

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)
}