package ru.goaliepash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.goaliepash.data.db.entity.PlaylistsEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistsEntity)
}