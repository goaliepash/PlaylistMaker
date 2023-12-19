package ru.goaliepash.data.itunes

import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.Response

interface ItunesClient {

    suspend fun search(searchRequest: SearchRequest): Response
}