package ru.goaliepash.playlistmaker.domain.use_case.search_history

import ru.goaliepash.playlistmaker.domain.api.SearchHistoryRepository
import ru.goaliepash.playlistmaker.domain.model.Track

class GetSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    fun execute(): Array<Track> {
        return searchHistoryRepository.get()
    }
}