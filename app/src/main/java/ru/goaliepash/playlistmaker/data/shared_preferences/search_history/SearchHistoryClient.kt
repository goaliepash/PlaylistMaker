package ru.goaliepash.playlistmaker.data.shared_preferences.search_history

import ru.goaliepash.playlistmaker.domain.model.Track

interface SearchHistoryClient {

    fun add(searchHistoryTracks: List<Track>)

    fun get(): Array<Track>

    fun clear()
}