package ru.goaliepash.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.data.dto.TrackDto
import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.SearchResponse
import ru.goaliepash.data.itunes.ItunesClient
import ru.goaliepash.data.utils.HttpStatusCode
import ru.goaliepash.domain.api.ItunesRepository
import ru.goaliepash.domain.model.Track

class ItunesRepositoryImpl(
    private val itunesClient: ItunesClient,
    private val appDatabase: AppDatabase
) : ItunesRepository {

    override fun getSearch(term: String): Flow<List<Track>> = flow {
        val response = itunesClient.search(SearchRequest(term))
        when (response.resultCode) {
            HttpStatusCode.OK -> {
                val tracks = (response as SearchResponse).results.map { trackDto: TrackDto ->
                    Track(
                        trackId = trackDto.trackId,
                        trackName = trackDto.trackName,
                        artistName = trackDto.artistName,
                        trackTimeMillis = trackDto.trackTimeMillis,
                        artworkUrl100 = trackDto.artworkUrl100.orEmpty(),
                        collectionName = trackDto.collectionName.orEmpty(),
                        releaseDate = trackDto.releaseDate.orEmpty(),
                        primaryGenreName = trackDto.primaryGenreName.orEmpty(),
                        country = trackDto.country.orEmpty(),
                        previewUrl = trackDto.previewUrl
                    )
                }
                setInFavoriteField(tracks)
                emit(tracks)
            }

            else -> {
                emit(emptyList())
            }
        }
    }

    private suspend fun setInFavoriteField(tracks: List<Track>) {
        val tracksIds = appDatabase.favoriteTracksDao().getFavoriteTracksIds()
        tracks.forEach {
            it.isFavorite = tracksIds.contains(it.trackId)
        }
    }
}