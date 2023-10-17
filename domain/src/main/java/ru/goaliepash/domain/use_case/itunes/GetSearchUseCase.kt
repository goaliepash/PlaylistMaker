package ru.goaliepash.domain.use_case.itunes

import ru.goaliepash.domain.api.ItunesRepository
import ru.goaliepash.domain.model.Track

class GetSearchUseCase(private val itunesRepository: ItunesRepository) {

    operator fun invoke(term: String): List<Track> {
        return itunesRepository.getSearch(term)
    }
}