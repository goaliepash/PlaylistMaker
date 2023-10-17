package ru.goaliepash.playlistmaker.util

import android.content.Context
import ru.goaliepash.data.itunes.ItunesClient
import ru.goaliepash.data.itunes.ItunesClientImpl
import ru.goaliepash.data.repository.AppThemeRepositoryImpl
import ru.goaliepash.data.repository.ItunesRepositoryImpl
import ru.goaliepash.data.repository.SearchHistoryRepositoryImpl
import ru.goaliepash.data.shared_preferences.app_theme.AppThemeClient
import ru.goaliepash.data.shared_preferences.app_theme.AppThemeClientImpl
import ru.goaliepash.data.shared_preferences.search_history.SearchHistoryClient
import ru.goaliepash.data.shared_preferences.search_history.SearchHistoryClientImpl
import ru.goaliepash.domain.api.AppThemeRepository
import ru.goaliepash.domain.api.ItunesRepository
import ru.goaliepash.domain.interactor.AppThemeInteractor
import ru.goaliepash.domain.interactor.ItunesInteractor
import ru.goaliepash.domain.interactor.SearchHistoryInteractor
import ru.goaliepash.domain.interactor.implementation.AppThemeInteractorImpl
import ru.goaliepash.domain.interactor.implementation.ItunesInteractorImpl
import ru.goaliepash.domain.interactor.implementation.SearchHistoryInteractorImpl

object Creator {

    fun provideAppThemeInteractor(context: Context): AppThemeInteractor {
        return AppThemeInteractorImpl(provideAppThemeRepository(context))
    }

    fun provideItunesInteractor(): ItunesInteractor {
        return ItunesInteractorImpl(provideItunesRepository())
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))
    }

    private fun provideAppThemeRepository(context: Context): AppThemeRepository {
        return AppThemeRepositoryImpl(provideAppThemeClient(context))
    }

    private fun provideItunesRepository(): ItunesRepository {
        return ItunesRepositoryImpl(provideItunesClient())
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepositoryImpl {
        return SearchHistoryRepositoryImpl(provideSearchHistoryClient(context))
    }

    private fun provideAppThemeClient(context: Context): AppThemeClient {
        return AppThemeClientImpl(context)
    }

    private fun provideItunesClient(): ItunesClient {
        return ItunesClientImpl()
    }

    private fun provideSearchHistoryClient(context: Context): SearchHistoryClient {
        return SearchHistoryClientImpl(context)
    }
}