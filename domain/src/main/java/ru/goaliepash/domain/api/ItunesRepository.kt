package ru.goaliepash.domain.api

import ru.goaliepash.domain.model.Track

interface ItunesRepository {

    fun getSearch(term: String): List<Track>
}