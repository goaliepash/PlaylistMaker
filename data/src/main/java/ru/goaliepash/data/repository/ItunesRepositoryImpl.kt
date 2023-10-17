package ru.goaliepash.data.repository

import ru.goaliepash.data.dto.TrackDto
import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.SearchResponse
import ru.goaliepash.data.itunes.ItunesClient
import ru.goaliepash.domain.api.ItunesRepository
import ru.goaliepash.domain.model.Track

class ItunesRepositoryImpl(private val itunesClient: ItunesClient) : ItunesRepository {

    override fun getSearch(term: String): List<Track> {
        val response = itunesClient.search(SearchRequest(term))
        return if (response.resultCode == 200) {
            (response as SearchResponse).results.map { trackDto: TrackDto ->
                Track(
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
        } else {
            emptyList()
        }
    }
}