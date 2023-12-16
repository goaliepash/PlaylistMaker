package ru.goaliepash.data.itunes

import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.Response

class ItunesClientImpl(private val itunesService: ItunesService) : ItunesClient {

    override suspend fun search(searchRequest: SearchRequest): Response {
        val response = itunesService.search(searchRequest.term)
        return if (response.resultCount == 0) {
            response.apply { resultCode = -1 }
        } else {
            response.apply { resultCode = 200 }
        }
    }
}