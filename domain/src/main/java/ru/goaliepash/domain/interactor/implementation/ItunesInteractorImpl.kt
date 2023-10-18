package ru.goaliepash.domain.interactor.implementation

import ru.goaliepash.domain.api.ItunesRepository
import ru.goaliepash.domain.interactor.ItunesInteractor
import ru.goaliepash.domain.model.Track

class ItunesInteractorImpl(private val itunesRepository: ItunesRepository) : ItunesInteractor {

    override fun getSearch(term: String): List<Track> {
        return itunesRepository.getSearch(term)
    }
}