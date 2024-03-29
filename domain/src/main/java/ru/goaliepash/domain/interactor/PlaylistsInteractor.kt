package ru.goaliepash.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track

interface PlaylistsInteractor {

    suspend fun addPlaylists(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addToPlaylist(track: Track)

    suspend fun updatePlaylist(playlistId: Int, trackIds: List<String>, tracksCount: Int)

    fun getPlaylist(id: Int): Flow<Playlist>

    fun getPlaylistTracks(trackIds: List<String>): Flow<List<Track>>

    fun deleteTrackFromPlaylist(trackId: String, playlistId: Int): Flow<Playlist>

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylistInfo(id: Int, name: String, description: String, coverUri: String)
}