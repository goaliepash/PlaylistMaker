package ru.goaliepash.playlistmaker.data.network.itunes

import ru.goaliepash.playlistmaker.data.dto.response.Response

interface ItunesClient {

    fun search(dto: Any): Response
}