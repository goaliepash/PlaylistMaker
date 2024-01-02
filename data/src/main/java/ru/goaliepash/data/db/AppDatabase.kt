package ru.goaliepash.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.goaliepash.data.db.dao.FavoriteTracksDao
import ru.goaliepash.data.db.entity.FavoriteTracksEntity

@Database(version = 1, entities = [FavoriteTracksEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteTracksDao
}