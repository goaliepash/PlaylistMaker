package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.converter.FavoriteTracksConverter
import ru.goaliepash.data.converter.PlaylistTracksConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.domain.api.FavoriteTracksRepository
import ru.goaliepash.domain.model.Track

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteTracksConverter: FavoriteTracksConverter
) : FavoriteTracksRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase.favoriteTracksDao().getFavoriteTracks()
        emit(favoriteTracks.map(favoriteTracksConverter::map))
    }

    override suspend fun addFavoriteTrack(favoriteTrack: Track) {
        appDatabase.favoriteTracksDao().insertFavoriteTrack(favoriteTracksConverter.map(favoriteTrack))
    }

    override suspend fun deleteFavoriteTrack(favoriteTrack: Track) {
        appDatabase.favoriteTracksDao().deleteFavoriteTrack(favoriteTracksConverter.map(favoriteTrack))
    }
}