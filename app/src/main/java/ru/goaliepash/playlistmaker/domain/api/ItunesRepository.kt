package ru.goaliepash.playlistmaker.domain.api

import ru.goaliepash.playlistmaker.domain.model.Track

interface ItunesRepository {

    fun getSearch(term: String): List<Track>
}