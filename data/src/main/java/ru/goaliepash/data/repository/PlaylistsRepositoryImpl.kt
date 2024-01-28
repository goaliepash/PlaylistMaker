package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.converter.PlaylistsDbConverter
import ru.goaliepash.data.converter.TracksDbConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.domain.api.PlaylistsRepository
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistsDbConverter: PlaylistsDbConverter,
    private val tracksDbConverter: TracksDbConverter
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().insertPlaylist(playlistsDbConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getPlaylists()
        emit(playlists.map(playlistsDbConverter::map))
    }

    override suspend fun addToPlaylist(track: Track) {
        appDatabase.playlistTracksDao().addToPlaylist(tracksDbConverter.map(track))
    }

    override suspend fun updatePlaylist(trackIds: List<String>, tracksCount: Int, dateAdded: Long) {
        appDatabase.playlistsDao().updatePlaylist(
            trackIds = playlistsDbConverter.map(trackIds),
            tracksCount = tracksCount.toString(),
            dateAdded = dateAdded.toString()
        )
    }
}