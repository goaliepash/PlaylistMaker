package ru.goaliepash.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Track

interface FavoriteTracksInteractor {

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun addFavoriteTrack(favoriteTrack: Track)

    suspend fun deleteFavoriteTrack(favoriteTrack: Track)

    fun isExists(trackId: String): Flow<Boolean>
}