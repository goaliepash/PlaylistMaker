package ru.goaliepash.playlistmaker.domain.use_case.itunes

import ru.goaliepash.playlistmaker.domain.api.ItunesRepository
import ru.goaliepash.playlistmaker.domain.model.Track

class GetSearchUseCase(private val itunesRepository: ItunesRepository) {

    fun execute(term: String): List<Track> {
        return itunesRepository.getSearch(term)
    }
}