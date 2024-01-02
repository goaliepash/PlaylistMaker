package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.converter.FavoriteTracksDbConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.domain.api.FavoriteTracksRepository
import ru.goaliepash.domain.model.Track

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteTracksDbConverter: FavoriteTracksDbConverter
) : FavoriteTracksRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase.favoriteTracksDao().getFavoriteTracks()
        emit(favoriteTracks.map(favoriteTracksDbConverter::map))
    }

    override suspend fun addFavoriteTrack(favoriteTrack: Track) {
        appDatabase.favoriteTracksDao().insertFavoriteTrack(favoriteTracksDbConverter.map(favoriteTrack))
    }

    override suspend fun deleteFavoriteTrack(favoriteTrack: Track) {
        appDatabase.favoriteTracksDao().deleteFavoriteTrack(favoriteTracksDbConverter.map(favoriteTrack))
    }
}