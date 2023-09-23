package ru.goaliepash.playlistmaker.domain.use_case.search_history

import ru.goaliepash.playlistmaker.domain.api.SearchHistoryRepository

class ClearSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    operator fun invoke() {
        searchHistoryRepository.clear()
    }
}