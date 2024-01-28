package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.goaliepash.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToPlaylist(track: PlaylistTrackEntity)
}