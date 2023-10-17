package ru.goaliepash.data.shared_preferences.search_history

import ru.goaliepash.domain.model.Track

interface SearchHistoryClient {

    fun add(searchHistoryTracks: List<Track>)

    fun get(): Array<Track>

    fun clear()
}