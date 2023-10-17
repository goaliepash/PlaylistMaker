package ru.goaliepash.playlistmaker.di

import org.koin.dsl.module
import ru.goaliepash.domain.interactor.AppThemeInteractor
import ru.goaliepash.domain.interactor.ItunesInteractor
import ru.goaliepash.domain.interactor.SearchHistoryInteractor
import ru.goaliepash.domain.interactor.implementation.AppThemeInteractorImpl
import ru.goaliepash.domain.interactor.implementation.ItunesInteractorImpl
import ru.goaliepash.domain.interactor.implementation.SearchHistoryInteractorImpl

val domainModule = module {

    factory<AppThemeInteractor> {
        AppThemeInteractorImpl(appThemeRepository = get())
    }

    factory<ItunesInteractor> {
        ItunesInteractorImpl(itunesRepository = get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(searchHistoryRepository = get())
    }
}