package ru.goaliepash.data.itunes

import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.Response
import ru.goaliepash.data.utils.HttpStatusCode

class ItunesClientImpl(private val itunesService: ItunesService) : ItunesClient {

    override suspend fun search(searchRequest: SearchRequest): Response {
        val response = itunesService.search(searchRequest.term)
        return if (response.resultCount == 0) {
            response.apply {
                resultCode = HttpStatusCode.NO_CONTENT
            }
        } else {
            response.apply {
                resultCode = HttpStatusCode.OK
            }
        }
    }
}