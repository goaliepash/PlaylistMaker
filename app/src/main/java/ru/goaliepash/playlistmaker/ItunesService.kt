package ru.goaliepash.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesService {

    private const val BASE_URL = "https://itunes.apple.com"

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getItunesApi(): ItunesApi {
        return retrofit.create(ItunesApi::class.java)
    }
}