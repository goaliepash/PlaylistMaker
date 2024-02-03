package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.goaliepash.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToPlaylist(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId IN (:trackIds)")
    suspend fun getPlaylistTracks(trackIds: List<String>): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: String)
}