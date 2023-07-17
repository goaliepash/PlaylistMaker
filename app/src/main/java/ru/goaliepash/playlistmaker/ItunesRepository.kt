package ru.goaliepash.playlistmaker

import retrofit2.Call
import ru.goaliepash.playlistmaker.model.SearchResponse

class ItunesRepository(private val itunesApi: ItunesApi) {

    fun getSearch(term: String): Call<SearchResponse> {
        return itunesApi.search(term)
    }
}