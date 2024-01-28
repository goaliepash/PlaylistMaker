package ru.goaliepash.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val description: String,
    val trackIds: String,
    val tracksCount: String,
    val coverUri: String,
    val dateAdded: String
)