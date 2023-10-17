package ru.goaliepash.domain.use_case.search_history

import ru.goaliepash.domain.api.SearchHistoryRepository

class ClearSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    operator fun invoke() {
        searchHistoryRepository.clear()
    }
}