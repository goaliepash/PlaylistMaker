package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.goaliepash.data.db.entity.PlaylistsEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistsEntity)

    @Query("SELECT * FROM playlists ORDER BY dateAdded DESC")
    suspend fun getPlaylists(): List<PlaylistsEntity>

    @Query("UPDATE playlists SET trackIds = :trackIds, tracksCount = :tracksCount WHERE dateAdded = :dateAdded")
    suspend fun updatePlaylist(trackIds: String, tracksCount: String, dateAdded: String)
}