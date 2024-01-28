package ru.goaliepash.data.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.goaliepash.data.db.entity.PlaylistsEntity
import ru.goaliepash.domain.model.Playlist

class PlaylistsDbConverter(private val gson: Gson) {

    fun map(playlist: Playlist): PlaylistsEntity {
        val trackIds: String = map(playlist.trackIds)
        return PlaylistsEntity(
            name = playlist.name,
            description = playlist.description,
            trackIds = trackIds,
            tracksCount = playlist.tracksCount.toString(),
            coverUri = playlist.coverUri,
            dateAdded = playlist.dateAdded.toString()
        )
    }

    fun map(playlistsEntity: PlaylistsEntity): Playlist {
        val type = object : TypeToken<List<String>>() {}.type
        val trackIds = gson.fromJson<List<String>>(playlistsEntity.trackIds, type)
        return Playlist(
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
}