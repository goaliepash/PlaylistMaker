package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.converter.PlaylistsConverter
import ru.goaliepash.data.converter.PlaylistTracksConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.domain.api.PlaylistsRepository
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track

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

    override suspend fun updatePlaylist(trackIds: List<String>, tracksCount: Int, dateAdded: Long) {
        appDatabase.playlistsDao().updatePlaylist(
            trackIds = playlistsConverter.map(trackIds),
            tracksCount = tracksCount.toString(),
            dateAdded = dateAdded.toString()
        )
    }
}