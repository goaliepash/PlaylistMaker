package ru.goaliepash.playlistmaker.data.repository

import ru.goaliepash.playlistmaker.data.shared_preferences.search_history.SearchHistoryClient
import ru.goaliepash.playlistmaker.domain.api.SearchHistoryRepository
import ru.goaliepash.playlistmaker.domain.model.Track

class SearchHistoryRepositoryImpl(private val searchHistoryClient: SearchHistoryClient) : SearchHistoryRepository {

    override fun add(searchHistoryTracks: List<Track>) {
        searchHistoryClient.add(searchHistoryTracks)
    }

    override fun get(): Array<Track> {
        return searchHistoryClient.get()
    }

    override fun clear() {
        searchHistoryClient.clear()
    }
}