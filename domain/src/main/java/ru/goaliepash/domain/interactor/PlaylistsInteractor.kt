package ru.goaliepash.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Playlist

interface PlaylistsInteractor {

    suspend fun addPlaylists(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>
}