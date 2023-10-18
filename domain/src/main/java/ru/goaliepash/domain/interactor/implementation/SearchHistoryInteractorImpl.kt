package ru.goaliepash.domain.interactor.implementation

import ru.goaliepash.domain.api.SearchHistoryRepository
import ru.goaliepash.domain.interactor.SearchHistoryInteractor
import ru.goaliepash.domain.model.Track

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) : SearchHistoryInteractor {

    override fun addSearchHistory(searchHistoryTracks: List<Track>) {
        searchHistoryRepository.add(searchHistoryTracks)
    }

    override fun getSearchHistory(): Array<Track> {
        return searchHistoryRepository.get()
    }

    override fun clearSearchHistory() {
        searchHistoryRepository.clear()
    }
}