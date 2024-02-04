package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("UPDATE playlists SET trackIds = :trackIds, tracksCount = :tracksCount WHERE id = :playlistId")
    suspend fun updatePlaylist(playlistId: Int, trackIds: String, tracksCount: String)

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylist(id: Int): PlaylistsEntity

    @Query("UPDATE playlists SET trackIds = :trackIds, tracksCount = tracksCount - 1 WHERE id = :playlistId")
    suspend fun updateTracksInPlaylist(trackIds: String, playlistId: Int)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistsEntity)

    @Query("UPDATE playlists SET name = :name, description = :description, coverUri = :coverUri WHERE id = :id")
    suspend fun updatePlaylistInfo(id: Int, name: String, description: String, coverUri: String)
}