package ru.goaliepash.data.itunes

import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.Response

class ItunesClientImpl(private val itunesService: ItunesService) : ItunesClient {

    override fun search(searchRequest: SearchRequest): Response {
        val response = itunesService.search(searchRequest.term).execute()
        val body = response.body() ?: Response()
        return body.apply { resultCode = response.code() }
    }
}