package ru.goaliepash.playlistmaker.domain.use_case.search_history

import ru.goaliepash.playlistmaker.domain.api.SearchHistoryRepository
import ru.goaliepash.playlistmaker.domain.model.Track

class AddSearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    fun execute(searchHistoryTracks: List<Track>) {
        searchHistoryRepository.add(searchHistoryTracks)
    }
}