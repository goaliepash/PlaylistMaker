package ru.goaliepash.data.itunes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.goaliepash.data.dto.request.SearchRequest
import ru.goaliepash.data.dto.response.Response

class ItunesClientImpl : ItunesClient {

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesService::class.java)

    override fun search(searchRequest: SearchRequest): Response {
        val response = itunesService.search(searchRequest.term).execute()
        val body = response.body() ?: Response()
        return body.apply { resultCode = response.code() }
    }
}