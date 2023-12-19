package ru.goaliepash.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Track

interface ItunesInteractor {

    fun getSearch(term: String): Flow<List<Track>>
}