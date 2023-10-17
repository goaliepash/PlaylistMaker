package ru.goaliepash.domain.use_case.search_history

import ru.goaliepash.domain.api.SearchHistoryRepository
import ru.goaliepash.domain.model.Track

class GetSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    operator fun invoke(): Array<Track> {
        return searchHistoryRepository.get()
    }
}