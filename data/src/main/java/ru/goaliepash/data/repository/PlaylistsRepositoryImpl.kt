package ru.goaliepash.data.repository

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
}