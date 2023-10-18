package ru.goaliepash.domain.api

import ru.goaliepash.domain.model.Track

interface SearchHistoryRepository {

    fun add(searchHistoryTracks: List<Track>)

    fun get(): Array<Track>

    fun clear()
}