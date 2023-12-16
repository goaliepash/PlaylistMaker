package ru.goaliepash.domain.api

import kotlinx.coroutines.flow.Flow
import ru.goaliepash.domain.model.Track

interface ItunesRepository {

    fun getSearch(term: String): Flow<List<Track>>
}