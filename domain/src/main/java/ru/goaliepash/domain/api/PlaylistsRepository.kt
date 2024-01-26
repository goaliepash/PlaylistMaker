package ru.goaliepash.domain.api

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Playlist

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}