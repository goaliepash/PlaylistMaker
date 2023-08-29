package ru.goaliepash.playlistmaker

import retrofit2.Call

class ItunesRepository(private val itunesApi: ItunesApi) {

    fun getSearch(term: String): Call<SearchResponse> {
        return itunesApi.search(term)
    }
}