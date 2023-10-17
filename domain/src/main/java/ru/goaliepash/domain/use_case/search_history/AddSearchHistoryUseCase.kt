package ru.goaliepash.domain.use_case.search_history

import ru.goaliepash.domain.api.SearchHistoryRepository
import ru.goaliepash.domain.model.Track

class AddSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    operator fun invoke(searchHistoryTracks: List<Track>) {
        searchHistoryRepository.add(searchHistoryTracks)
    }
}