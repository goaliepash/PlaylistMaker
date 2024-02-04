package ru.goaliepash.data.converter

import ru.goaliepash.data.db.entity.PlaylistTrackEntity
import ru.goaliepash.domain.model.Track

class PlaylistTracksConverter {

    fun map(playlistTrackEntity: PlaylistTrackEntity): Track = Track(
        trackId = playlistTrackEntity.trackId,
        trackName = playlistTrackEntity.trackName,
        artistName = playlistTrackEntity.artistName,
        trackTimeMillis = playlistTrackEntity.trackTimeMillis,
        artworkUrl100 = playlistTrackEntity.artworkUrl100,
        collectionName = playlistTrackEntity.collectionName,
        releaseDate = playlistTrackEntity.releaseDate,
        primaryGenreName = playlistTrackEntity.primaryGenreName,
        country = playlistTrackEntity.country,
        previewUrl = playlistTrackEntity.previewUrl
    )

    fun map(track: Track): PlaylistTrackEntity = PlaylistTrackEntity(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTimeMillis = track.trackTimeMillis,
        artworkUrl100 = track.artworkUrl100,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        previewUrl = track.previewUrl
    )
}