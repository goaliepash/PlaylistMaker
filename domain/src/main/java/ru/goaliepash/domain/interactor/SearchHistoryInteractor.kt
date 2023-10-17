package ru.goaliepash.domain.interactor

import ru.goaliepash.domain.model.Track

interface SearchHistoryInteractor {

    fun addSearchHistory(searchHistoryTracks: List<Track>)

    fun getSearchHistory(): Array<Track>

    fun clearSearchHistory()
}