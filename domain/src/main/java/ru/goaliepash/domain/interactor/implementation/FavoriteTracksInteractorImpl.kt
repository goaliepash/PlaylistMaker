package ru.goaliepash.domain.interactor.implementation

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.api.FavoriteTracksRepository
import ru.goaliepash.domain.interactor.FavoriteTracksInteractor
import ru.goaliepash.domain.model.Track

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) : FavoriteTracksInteractor {

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }

    override suspend fun addFavoriteTrack(favoriteTrack: Track) {
        favoriteTracksRepository.addFavoriteTrack(favoriteTrack)
    }

    override suspend fun deleteFavoriteTrack(favoriteTrack: Track) {
        favoriteTracksRepository.deleteFavoriteTrack(favoriteTrack)
    }
}