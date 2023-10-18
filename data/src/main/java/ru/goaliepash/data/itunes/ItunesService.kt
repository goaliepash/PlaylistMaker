package ru.goaliepash.data.itunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.goaliepash.data.dto.response.SearchResponse

interface ItunesService {

    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<SearchResponse>
}