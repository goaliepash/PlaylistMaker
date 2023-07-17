package ru.goaliepash.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.goaliepash.playlistmaker.model.SearchResponse

interface ItunesApi {

    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<SearchResponse>
}