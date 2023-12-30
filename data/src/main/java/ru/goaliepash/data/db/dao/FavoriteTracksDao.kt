package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.goaliepash.data.db.entity.FavoriteTracksEntity

@Dao
interface FavoriteTracksDao {

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getFavoriteTracks(): List<FavoriteTracksEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(favoriteTracks: FavoriteTracksEntity)

    @Delete
    suspend fun deleteFavoriteTrack(favoriteTracks: FavoriteTracksEntity)

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getFavoriteTracksIds(): List<String>
}