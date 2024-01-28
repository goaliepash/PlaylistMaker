package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.goaliepash.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTracksDao {

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getFavoriteTracks(): List<FavoriteTrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(favoriteTracks: FavoriteTrackEntity)

    @Delete
    suspend fun deleteFavoriteTrack(favoriteTracks: FavoriteTrackEntity)

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getFavoriteTracksIds(): List<String>
}