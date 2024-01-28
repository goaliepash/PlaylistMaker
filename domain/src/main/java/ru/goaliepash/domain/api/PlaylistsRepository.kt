package ru.goaliepash.domain.api

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addToPlaylist(track: Track)

    suspend fun updatePlaylist(trackIds: List<String>, tracksCount: Int, dateAdded: Long)
}