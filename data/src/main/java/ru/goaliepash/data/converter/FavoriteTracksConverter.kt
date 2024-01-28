package ru.goaliepash.data.converter

import ru.goaliepash.data.db.entity.FavoriteTrackEntity
import ru.goaliepash.domain.model.Track

class FavoriteTracksConverter {

    fun map(favoriteTrackEntity: FavoriteTrackEntity): Track = Track(
        trackId = favoriteTrackEntity.trackId,
        trackName = favoriteTrackEntity.trackName,
        artistName = favoriteTrackEntity.artistName,
        trackTimeMillis = favoriteTrackEntity.trackTimeMillis,
        artworkUrl100 = favoriteTrackEntity.artworkUrl100,
        collectionName = favoriteTrackEntity.collectionName,
        releaseDate = favoriteTrackEntity.releaseDate,
        primaryGenreName = favoriteTrackEntity.primaryGenreName,
        country = favoriteTrackEntity.country,
        previewUrl = favoriteTrackEntity.previewUrl,
        isFavorite = true
    )

    fun map(track: Track): FavoriteTrackEntity = FavoriteTrackEntity(
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