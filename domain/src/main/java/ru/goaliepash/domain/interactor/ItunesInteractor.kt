package ru.goaliepash.domain.interactor

import ru.goaliepash.domain.model.Track

interface ItunesInteractor {

    fun getSearch(term: String): List<Track>
}