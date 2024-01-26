package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.converter.PlaylistsDbConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.domain.api.PlaylistsRepository
import ru.goaliepash.domain.model.Playlist

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistsDbConverter: PlaylistsDbConverter
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().insertPlaylist(playlistsDbConverter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getPlaylists()
        emit(playlists.map(playlistsDbConverter::map))
    }
}