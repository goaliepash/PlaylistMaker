package ru.goaliepash.playlistmaker.data.network.itunes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.goaliepash.playlistmaker.data.dto.request.SearchRequest
import ru.goaliepash.playlistmaker.data.dto.response.Response

class ItunesClientImpl : ItunesClient {

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesService::class.java)

    override fun search(dto: Any): Response {
        return if (dto is SearchRequest) {
            val response = itunesService.search(dto.term).execute()
            val body = response.body() ?: Response()
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}