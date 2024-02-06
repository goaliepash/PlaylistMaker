package ru.goaliepash.data.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.goaliepash.data.db.entity.PlaylistsEntity
import ru.goaliepash.domain.model.Playlist
import java.util.LinkedList

class PlaylistsConverter(private val gson: Gson) {

    fun map(playlist: Playlist): PlaylistsEntity {
        val trackIds: String = map(playlist.trackIds)
        return PlaylistsEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            trackIds = trackIds,
            tracksCount = playlist.tracksCount.toString(),
            coverUri = playlist.coverUri,
            dateAdded = playlist.dateAdded.toString()
        )
    }

    fun map(playlistsEntity: PlaylistsEntity): Playlist {
        val trackIds = map(playlistsEntity.trackIds)
        return Playlist(
            id = playlistsEntity.id,
            name = playlistsEntity.name,
            description = playlistsEntity.description,
            trackIds = trackIds,
            tracksCount = playlistsEntity.tracksCount.toInt(),
            coverUri = playlistsEntity.coverUri,
            dateAdded = playlistsEntity.dateAdded.toLong()
        )
    }

    fun map(trackIds: List<String>): String {
        return gson.toJson(trackIds)
    }

    fun map(trackIds: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(trackIds, type)
    }
}