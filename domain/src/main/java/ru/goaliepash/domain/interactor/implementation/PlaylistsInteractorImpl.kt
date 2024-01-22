package ru.goaliepash.domain.interactor.implementation

import ru.goaliepash.domain.api.PlaylistsRepository
import ru.goaliepash.domain.interactor.PlaylistsInteractor
import ru.goaliepash.domain.model.Playlist

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override suspend fun addPlaylists(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }
}