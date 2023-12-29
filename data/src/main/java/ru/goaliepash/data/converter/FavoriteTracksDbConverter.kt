package ru.goaliepash.data.converter

import ru.goaliepash.data.db.entity.FavoriteTracksEntity
import ru.goaliepash.domain.model.Track

class FavoriteTracksDbConverter {

    fun map(favoriteTracksEntity: FavoriteTracksEntity): Track = Track(
        trackId = favoriteTracksEntity.trackId,
        trackName = favoriteTracksEntity.trackName,
        artistName = favoriteTracksEntity.artistName,
        trackTimeMillis = favoriteTracksEntity.trackTimeMillis,
        artworkUrl100 = favoriteTracksEntity.artworkUrl100,
        collectionName = favoriteTracksEntity.collectionName,
        releaseDate = favoriteTracksEntity.releaseDate,
        primaryGenreName = favoriteTracksEntity.primaryGenreName,
        country = favoriteTracksEntity.country,
        previewUrl = favoriteTracksEntity.previewUrl
    )

    fun map(track: Track): FavoriteTracksEntity = FavoriteTracksEntity(
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