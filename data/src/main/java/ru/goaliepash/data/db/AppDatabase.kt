package ru.goaliepash.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.goaliepash.data.db.dao.FavoriteTracksDao
import ru.goaliepash.data.db.dao.PlaylistsDao
import ru.goaliepash.data.db.entity.FavoriteTracksEntity
import ru.goaliepash.data.db.entity.PlaylistsEntity

@Database(version = 1, entities = [FavoriteTracksEntity::class, PlaylistsEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteTracksDao

    abstract fun playlistsDao(): PlaylistsDao
}