package ru.goaliepash.domain.api

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Track

interface FavoriteTracksRepository {

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun addFavoriteTrack(favoriteTrack: Track)

    suspend fun deleteFavoriteTrack(favoriteTrack: Track)
}