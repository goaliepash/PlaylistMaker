package ru.goaliepash.domain.interactor.implementation

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.api.PlaylistsRepository
import ru.goaliepash.domain.interactor.PlaylistsInteractor
import ru.goaliepash.domain.model.Playlist
import ru.goaliepash.domain.model.Track

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override suspend fun addPlaylists(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun addToPlaylist(track: Track) {
        playlistsRepository.addToPlaylist(track)
    }

    override suspend fun updatePlaylist(playlistId: Int, trackIds: List<String>, tracksCount: Int) {
        playlistsRepository.updatePlaylist(playlistId, trackIds, tracksCount)
    }

    override fun getPlaylist(id: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylist(id)
    }

    override fun getPlaylistTracks(trackIds: List<String>): Flow<List<Track>> {
        return playlistsRepository.getPlaylistTracks(trackIds)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }
}