package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.converter.PlaylistTracksConverter
import ru.goaliepash.data.converter.PlaylistsConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.domain.api.PlaylistsRepository
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track
import java.util.LinkedList

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistsConverter: PlaylistsConverter,
    private val playlistTracksConverter: PlaylistTracksConverter
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().insertPlaylist(playlistsConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getPlaylists()
        emit(playlists.map(playlistsConverter::map))
    }

    override suspend fun addToPlaylist(track: Track) {
        appDatabase.playlistTracksDao().addToPlaylist(playlistTracksConverter.map(track))
    }

    override suspend fun updatePlaylist(playlistId: Int, trackIds: List<String>, tracksCount: Int) {
        appDatabase.playlistsDao().updatePlaylist(
            playlistId = playlistId,
            trackIds = playlistsConverter.map(trackIds),
            tracksCount = tracksCount.toString(),
        )
    }

    override fun getPlaylist(id: Int): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistsDao().getPlaylist(id)
        emit(playlistsConverter.map(playlist))
    }

    override fun getPlaylistTracks(trackIds: List<String>): Flow<List<Track>> = flow {
        val playlistTracks = appDatabase.playlistTracksDao().getPlaylistTracks(trackIds)
        emit(playlistTracks.map(playlistTracksConverter::map))
    }

    override fun deleteTrackFromPlaylist(trackId: String, playlistId: Int): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistsDao().getPlaylist(playlistId)
        val trackIds = LinkedList<String>()
        trackIds.addAll(playlistsConverter.map(playlist.trackIds).filter { it != trackId })
        appDatabase.playlistsDao().updateTracksInPlaylist(
            trackIds = playlistsConverter.map(trackIds),
            playlistId = playlistId
        )
        deleteTrackById(trackId)
        emit(playlistsConverter.map(appDatabase.playlistsDao().getPlaylist(playlistId)))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().deletePlaylist(playlistsConverter.map(playlist))
        playlist.trackIds.forEach {
            deleteTrackById(it)
        }
    }

    private suspend fun deleteTrackById(trackId: String) {
        val playlistsWithCurrentTrack = appDatabase
            .playlistsDao()
            .getPlaylists()
            .map(playlistsConverter::map)
            .count {
                it.trackIds.contains(trackId)
            }
        if (playlistsWithCurrentTrack == 0) {
            appDatabase.playlistTracksDao().deleteTrackById(trackId = trackId)
        }
    }

    override suspend fun updatePlaylistInfo(
        id: Int,
        name: String,
        description: String,
        coverUri: String
    ) {
        appDatabase.playlistsDao().updatePlaylistInfo(id, name, description, coverUri)
    }
}