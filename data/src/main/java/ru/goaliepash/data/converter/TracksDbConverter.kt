package ru.goaliepash.data.converter

import ru.goaliepash.data.db.entity.TrackEntity
import ru.goaliepash.domain.model.Track

class TracksDbConverter {

    fun map(trackEntity: TrackEntity): Track = Track(
        trackId = trackEntity.trackId,
        trackName = trackEntity.trackName,
        artistName = trackEntity.artistName,
        trackTimeMillis = trackEntity.trackTimeMillis,
        artworkUrl100 = trackEntity.artworkUrl100,
        collectionName = trackEntity.collectionName,
        releaseDate = trackEntity.releaseDate,
        primaryGenreName = trackEntity.primaryGenreName,
        country = trackEntity.country,
        previewUrl = trackEntity.previewUrl,
        isFavorite = true
    )

    fun map(track: Track): TrackEntity = TrackEntity(
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