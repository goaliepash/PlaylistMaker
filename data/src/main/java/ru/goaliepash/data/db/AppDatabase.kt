package ru.goaliepash.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.goaliepash.data.db.dao.FavoriteTracksDao
import ru.goaliepash.data.db.dao.PlaylistTracksDao
import ru.goaliepash.data.db.dao.PlaylistsDao
import ru.goaliepash.data.db.entity.FavoriteTrackEntity
import ru.goaliepash.data.db.entity.PlaylistsEntity
import ru.goaliepash.data.db.entity.PlaylistTrackEntity

@Database(
    version = 1,
    entities = [FavoriteTrackEntity::class, PlaylistTrackEntity::class, PlaylistsEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteTracksDao

    abstract fun playlistsDao(): PlaylistsDao

    abstract fun playlistTracksDao(): PlaylistTracksDao
}